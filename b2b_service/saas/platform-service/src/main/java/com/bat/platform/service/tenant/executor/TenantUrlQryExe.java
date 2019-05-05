package com.bat.platform.service.tenant.executor;

import static com.bat.platform.service.tenant.executor.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantUrlQry;
import com.bat.platform.api.tenant.dto.data.TenantUrlDTO;
import com.bat.platform.dao.tenant.PlatformTenantUrlMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantCommonDO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantUrlDO;
import com.bat.platform.service.tenant.convertor.TenantConvertor;

/**
 * 沙漠
 */
@Component
public class TenantUrlQryExe {

    @Resource
    private PlatformTenantUrlMapper platformTenantUrlMapper;

    @Resource
    private TenantCommonQryExe tenantCommonQryExe;

    public List<TenantUrlDTO> listByTenantId(Integer tenantId) {
        List<PlatformTenantUrlDO> platformTenantUrlDOS = platformTenantUrlMapper.listByTenantId(tenantId);
        List<TenantUrlDTO> tenantUrlDTOS = new ArrayList<>();
        for (PlatformTenantUrlDO platformTenantUrlDO : platformTenantUrlDOS) {
            TenantUrlDTO tenantUrlDTO = new TenantUrlDTO();
            BeanUtils.copyProperties(platformTenantUrlDO, tenantUrlDTO);
            tenantUrlDTOS.add(tenantUrlDTO);
        }
        return tenantUrlDTOS;
    }

    public PlatformTenantUrlRpcDTO urlConfig(Integer tenantId, Short urlType) {
        List<PlatformTenantUrlDO> platformTenantUrlDOS =
            platformTenantUrlMapper.selectByTenantIdAndUrlType(tenantId, urlType);
        if (CollectionUtils.isEmpty(platformTenantUrlDOS)) {
            return null;
        }
        PlatformTenantUrlRpcDTO platformTenantUrlRpcDTO = new PlatformTenantUrlRpcDTO();
        BeanUtils.copyProperties(platformTenantUrlDOS.get(0), platformTenantUrlRpcDTO);
        return platformTenantUrlRpcDTO;
    }

    /**
     * 获取租户地址
     *
     * @param qry
     * @return
     */
    public TenantUrlDTO get(TenantUrlQry qry) {
        PlatformTenantUrlDO urlDO = null;
        // 如果传了租户编码，直接通过租户编码获取，如果未传租户编码情况，间接通过主机域名获取
        String tenantNo = qry.getTenantNo();
        if (StringUtils.isBlank(qry.getTenantNo())) {
            if (StringUtils.isNotBlank(qry.getWxProgramAppId())) {
                List<PlatformTenantCommonDO> platformTenantCommonDOS =
                    tenantCommonQryExe.selectByWxProgramAppId(qry.getWxProgramAppId());
                if (CollectionUtils.isEmpty(platformTenantCommonDOS) || platformTenantCommonDOS.size() > 1) {
                    throw PlatformException.buildException(B_PLATFORM_WX_PROGRAM_APP_ID_ERROR);
                }
                tenantNo = platformTenantCommonDOS.get(0).getTenantNo();
            } else if (StringUtils.isNotBlank(qry.getHost())) {
                List<PlatformTenantUrlDO> tenantUrlDOS =
                    platformTenantUrlMapper.listByHostAndUrlType(qry.getHost(), qry.getQryUrlType());
                if (CollectionUtils.isEmpty(tenantUrlDOS) || tenantUrlDOS.size() > 1) {
                    throw PlatformException.buildException(B_PLATFORM_QRY_URL_ERROR);
                } else {
                    tenantNo = tenantUrlDOS.get(0).getTenantNo();
                }
            }
        }
        List<PlatformTenantUrlDO> tenantUrlDOS =
            platformTenantUrlMapper.selectByTenantNoAndUrlType(tenantNo, qry.getGainUrlType());
        if (CollectionUtils.isEmpty(tenantUrlDOS)) {
            throw PlatformException.buildException(B_PLATFORM_GAIN_URL_NULL);
        }
        urlDO = tenantUrlDOS.get(0);
        return TenantConvertor.getTenantUrlDTO(urlDO);
    }
}
