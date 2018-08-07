package com.bat.system.service.check;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.check.dto.data.CheckConfigDTO;
import com.bat.system.dao.check.dataobject.CheckConfigDO;
import com.bat.system.service.check.executor.CheckQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.check.api.SystemCheckServiceRpc;
import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.check.dto.data.CheckConfigRpcDTO;
import com.bat.dubboapi.system.common.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/15 15:29
 */
@DubboService
@Slf4j
public class CheckServiceRpcImpl implements SystemCheckServiceRpc {

    @Resource
    private CheckQryExc checkQryExc;

    @Override
    public Response<CheckConfigDetailRpcDTO> getCheckConfigDetail(Short ext) {
        List<CheckConfigDTO> checkConfigDTOS = checkQryExc.checkDetail(ext);
        if (checkConfigDTOS != null && checkConfigDTOS.size() == 1) {
            CheckConfigDTO checkConfigDTO = checkConfigDTOS.get(0);
            CheckConfigDetailRpcDTO rpcDTO = new CheckConfigDetailRpcDTO();
            BeanUtils.copyProperties(checkConfigDTO, rpcDTO);
            List<CheckConfigDO> checkConfigs = checkConfigDTO.getCheckConfigs();
            List<CheckConfigRpcDTO> collect = checkConfigs.stream().map(checkConfigDO -> {
                CheckConfigRpcDTO dto = new CheckConfigRpcDTO();
                BeanUtils.copyProperties(checkConfigDO, dto);
                return dto;
            }).collect(Collectors.toList());
            rpcDTO.setCheckConfigs(collect);
            return Response.of(rpcDTO);
        } else {
            CheckConfigDetailRpcDTO dto = new CheckConfigDetailRpcDTO();
            dto.setExt(ext);
            dto.setOpenFlag((short)0);
            return Response.of(dto);
        }
    }

}
