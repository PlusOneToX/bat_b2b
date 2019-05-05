package com.bat.platform.service.tenant;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.platform.service.tenant.convertor.TenantConvertor;
import com.bat.platform.service.tenant.executor.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.*;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantOssQry;
import com.bat.platform.api.tenant.dto.TenantSmsQry;
import com.bat.platform.api.tenant.dto.data.TenantDTO;
import com.bat.platform.api.tenant.dto.data.TenantOssDTO;
import com.bat.platform.api.tenant.dto.data.TenantSmsDTO;
import com.bat.platform.api.utils.MessageUtils;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantCommonDO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDBDO;
import com.bat.platform.service.common.CommonErrorCode;

@DubboService
public class TenantServiceRpcImpl implements PlatformTenantServiceRpc {

    @Resource
    private TenantDBQryExe tenantDBQryExe;

    @Resource
    private TenantDiyFactoryQryExe tenantDiyFactoryQryExe;

    @Resource
    private TenantErpQryExe tenantErpQryExe;

    @Resource
    private TenantOssQryExe tenantOssQryExe;

    @Resource
    private TenantSmsQryExe tenantSmsQryExe;

    @Resource
    private TenantUrlQryExe tenantUrlQryExe;

    @Resource
    private TenantCommonQryExe tenantCommonQryExe;

    @Resource
    private TenantQryExe tenantQryExe;

    private static final Logger log = LoggerFactory.getLogger(TenantServiceRpcImpl.class);

    @Override
    public Response<List<PlatformTenantDBRpcDTO>> listTenantDBByModelType(Short modelType) {
        try {
            List<PlatformTenantDBDO> dbdos = tenantDBQryExe.listTenantDBByModelType(modelType);
            return Response.of(TenantConvertor.toPlatformTenantDBRpcDTOList(dbdos));
        } catch (PlatformException e) {
            log.info("获取所有租户数据库信息出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取所有租户数据库信息出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantDBRpcDTO> dbConfig(String tenantNo, Short modelType) {
        try {
            // 通过租户编码获取租户id
            if (StringUtils.isBlank(tenantNo)) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_TENANT_NO_NULL);
            }
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            PlatformTenantDBRpcDTO platformTenantDBRpcDTO = tenantDBQryExe.dbConfig(tenantId, modelType);
            if (platformTenantDBRpcDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            return Response.of(platformTenantDBRpcDTO);
        } catch (PlatformException e) {
            log.info("获取租户数据库信息出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取租户数据库信息出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantDiyFactoryRpcDTO> diyFactoryConfig(String tenantNo, String factoryNo) {
        try {
            // 通过租户编码获取租户id
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            PlatformTenantDiyFactoryRpcDTO platformTenantDiyFactoryRpcDTO =
                tenantDiyFactoryQryExe.diyFactoryConfig(tenantId, factoryNo);
            if (platformTenantDiyFactoryRpcDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            return Response.of(platformTenantDiyFactoryRpcDTO);
        } catch (PlatformException e) {
            log.info("获取工厂配置出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取工厂配置出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantErpRpcDTO> erpConfig(String tenantNo, Short erpType) {
        try {
            // 通过租户编码获取租户id
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            PlatformTenantErpRpcDTO platformTenantErpRpcDTO = tenantErpQryExe.erpConfig(tenantId, erpType);
            if (platformTenantErpRpcDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            return Response.of(platformTenantErpRpcDTO);
        } catch (PlatformException e) {
            log.info("获取erp配置出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取erp配置出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantOssRpcDTO> ossConfig(String tenantNo, Short ossType) {
        try {
            // 通过租户编码获取租户id
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            TenantOssQry qry = new TenantOssQry();
            qry.setTenantId(tenantId);
            qry.setOssType(ossType);
            TenantOssDTO tenantOssDTO = tenantOssQryExe.config(qry);
            if (tenantOssDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            PlatformTenantOssRpcDTO platformTenantOssRpcDTO = new PlatformTenantOssRpcDTO();
            BeanUtils.copyProperties(tenantOssDTO, platformTenantOssRpcDTO);
            return Response.of(platformTenantOssRpcDTO);
        } catch (PlatformException e) {
            log.info("获取oss配置出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取oss配置出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantSmsRpcDTO> smsConfig(String tenantNo, Short smsType) {
        try {
            // 通过租户编码获取租户id
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            TenantSmsQry qry = new TenantSmsQry();
            qry.setTenantId(tenantId);
            qry.setSmsType(smsType);
            TenantSmsDTO tenantSmsDTO = tenantSmsQryExe.config(qry);
            if (tenantSmsDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            PlatformTenantSmsRpcDTO platformTenantSmsRpcDTO = new PlatformTenantSmsRpcDTO();
            BeanUtils.copyProperties(tenantSmsDTO, platformTenantSmsRpcDTO);
            List<PlatformTenantSmsRpcDTO.TenantSmsTemplate> tenantSmsTemplates = new ArrayList<>();
            platformTenantSmsRpcDTO.setTenantSmsTemplates(tenantSmsTemplates);
            if (!CollectionUtils.isEmpty(tenantSmsDTO.getTenantSmsTemplates())) {
                tenantSmsDTO.getTenantSmsTemplates().forEach(tenantSmsTemplate -> {
                    PlatformTenantSmsRpcDTO.TenantSmsTemplate template =
                        new PlatformTenantSmsRpcDTO.TenantSmsTemplate();
                    BeanUtils.copyProperties(tenantSmsTemplate, template);
                    tenantSmsTemplates.add(template);
                });
            }
            return Response.of(platformTenantSmsRpcDTO);
        } catch (PlatformException e) {
            log.info("获取短信配置出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取短信配置出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response urlConfig(String tenantNo, Short urlType) {
        try {
            // 通过租户编码获取租户id
            Integer tenantId = tenantQryExe.getTenantIdByTenantNo(tenantNo);
            PlatformTenantUrlRpcDTO platformTenantUrlRpcDTO = tenantUrlQryExe.urlConfig(tenantId, urlType);
            if (platformTenantUrlRpcDTO == null) {
                throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
            }
            return Response.of(platformTenantUrlRpcDTO);
        } catch (PlatformException e) {
            log.info("获取url配置出现异常:{}", e);
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("获取url配置出现异常:{}", e);
            return Response.buildFailure(CommonErrorCode.D_COMMON_SYSTEM_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_SYSTEM_ERROR));
        }
    }

    @Override
    public Response<PlatformTenantCommonRpcDTO> commonConfig(String tenantNo) {
        PlatformTenantCommonDO platformTenantCommonDO = tenantCommonQryExe.selectByTenantNo(tenantNo);
        PlatformTenantCommonRpcDTO tenantCommonRpcDTO = new PlatformTenantCommonRpcDTO();
        BeanUtils.copyProperties(platformTenantCommonDO, tenantCommonRpcDTO);
        return Response.of(tenantCommonRpcDTO);
    }

    @Override
    public Response<PlatformTenantRpcDTO> baseConfig(String tenantNo) {
        TenantDTO detail = tenantQryExe.getDetailByTenantNo(tenantNo);
        PlatformTenantRpcDTO rpcDTO = new PlatformTenantRpcDTO();
        BeanUtils.copyProperties(detail, rpcDTO);
        return Response.of(rpcDTO);
    }

    @Override
    public Response<List<String>> factoryNoList() {
        List<String> factoryNoList = tenantDiyFactoryQryExe.factoryNoList();
        return Response.of(factoryNoList);
    }
}
