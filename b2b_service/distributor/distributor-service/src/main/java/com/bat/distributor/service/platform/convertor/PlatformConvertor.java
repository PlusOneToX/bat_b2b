package com.bat.distributor.service.platform.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.platform.dto.*;
import com.bat.distributor.api.platform.dto.data.*;
import com.bat.distributor.dao.platform.dataobject.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.distributor.api.platform.dto.*;
import com.bat.distributor.api.platform.dto.data.*;
import com.bat.distributor.dao.platform.dataobject.*;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;

public class PlatformConvertor {
    public static PlatformDO toPlatformDO(PlatformCmd cmd) {
        PlatformDO platformDO = new PlatformDO();
        BeanUtils.copyProperties(cmd, platformDO);
        Date date = new Date(System.currentTimeMillis());
        platformDO.setCreateTime(date);
        platformDO.setUpdateTime(date);
        return platformDO;
    }

    public static List<PlatformDTO> toPlatformDTOList(List<PlatformDO> doList) {
        List<PlatformDTO> dtoList = new ArrayList<>();
        doList.forEach(platformDO -> {
            PlatformDTO dto = new PlatformDTO();
            BeanUtils.copyProperties(platformDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static PlatformDTO toPlatformDTO(PlatformDO platformDO) {
        PlatformDTO dto = new PlatformDTO();
        BeanUtils.copyProperties(platformDO, dto);
        return dto;
    }

    public static SysPlatformDO toSysPlatformDO(SysPlatformCmd cmd) {
        SysPlatformDO platformDO = new SysPlatformDO();
        BeanUtils.copyProperties(cmd, platformDO);
        Date date = new Date(System.currentTimeMillis());
        platformDO.setCreateTime(date);
        platformDO.setUpdateTime(date);
        return platformDO;
    }

    public static WxPlatformDO toWxPlatformDO(WxPlatformCmd cmd) {
        WxPlatformDO platformDO = new WxPlatformDO();
        BeanUtils.copyProperties(cmd, platformDO);
        Date date = new Date(System.currentTimeMillis());
        platformDO.setCreateTime(date);
        platformDO.setUpdateTime(date);
        return platformDO;
    }

    public static List<SysPlatformListDTO> toSysPlatformListDTOList(List<SysPlatformListDO> doList) {
        List<SysPlatformListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(platformDO -> {
                SysPlatformListDTO dto = new SysPlatformListDTO();
                BeanUtils.copyProperties(platformDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static List<WxPlatformListDTO> toWxPlatformListDTOList(List<WxPlatformDO> doList) {
        List<WxPlatformListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(platformDO -> {
                WxPlatformListDTO dto = new WxPlatformListDTO();
                BeanUtils.copyProperties(platformDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static SysPlatformDTO toSysPlatformDTO(SysPlatformDO platformDO) {
        SysPlatformDTO dto = new SysPlatformDTO();
        BeanUtils.copyProperties(platformDO, dto);
        return dto;
    }

    public static WxPlatformDTO toWxPlatformDTO(WxPlatformDO platformDO) {
        WxPlatformDTO dto = new WxPlatformDTO();
        BeanUtils.copyProperties(platformDO, dto);
        return dto;
    }

    public static List<SysPlatformApiDO> toSysPlatformApiDOList(Integer sysPlatformId, List<SysPlatformApiCmd> cmds) {
        List<SysPlatformApiDO> apiDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                SysPlatformApiDO apiDO = new SysPlatformApiDO();
                BeanUtils.copyProperties(cmd, apiDO);
                apiDO.setSysPlatformId(sysPlatformId);
                apiDOS.add(apiDO);
            });
        }
        return apiDOS;
    }

    public static List<SysPlatformDistributorDO> toSysPlatformDistributorDOList(Integer sysPlatformId,
        List<PlatformDistributorCmd> cmds) {
        List<SysPlatformDistributorDO> distributorDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                SysPlatformDistributorDO distributorDO = new SysPlatformDistributorDO();
                BeanUtils.copyProperties(cmd, distributorDO);
                distributorDO.setSysPlatformId(sysPlatformId);
                distributorDOS.add(distributorDO);
            });
        }
        return distributorDOS;
    }

    public static List<WxPlatformDistributorDO> toWxPlatformDistributorDOList(Integer wxPlatformId,
        List<PlatformDistributorCmd> cmds) {
        List<WxPlatformDistributorDO> distributorDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                WxPlatformDistributorDO distributorDO = new WxPlatformDistributorDO();
                BeanUtils.copyProperties(cmd, distributorDO);
                distributorDO.setWxPlatformId(wxPlatformId);
                distributorDOS.add(distributorDO);
            });
        }
        return distributorDOS;
    }

    public static List<SysPlatformApiDTO> toSysPlatformApiDTOList(List<SysPlatformApiDO> apiDOS) {
        List<SysPlatformApiDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(apiDOS)) {
            apiDOS.forEach(apiDO -> {
                SysPlatformApiDTO apiDTO = new SysPlatformApiDTO();
                BeanUtils.copyProperties(apiDO, apiDTO);
                dtos.add(apiDTO);
            });
        }
        return dtos;
    }

    public static List<PlatformDistributorDTO> toPlatformDistributorDTOList(List dos) {
        List<PlatformDistributorDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dos)) {
            dos.forEach(item -> {
                PlatformDistributorDTO distributorDTO = new PlatformDistributorDTO();
                BeanUtils.copyProperties(item, distributorDTO);
                dtos.add(distributorDTO);
            });
        }
        return dtos;
    }

    public static DistributorPlatformApiRpcDTO
        toDistributorPlatformApiRpcDTO(DistributorPlatformApiRpcDO distributorPlatformApiRpcDO) {
        DistributorPlatformApiRpcDTO apiRpcDTO = new DistributorPlatformApiRpcDTO();
        BeanUtils.copyProperties(distributorPlatformApiRpcDO, apiRpcDTO);
        return apiRpcDTO;
    }

    public static SysPlatformRpcDTO toSysPlatformRpcDTO(SysPlatformDO sysPlatformDO) {
        SysPlatformRpcDTO sysPlatformRpcDTO = new SysPlatformRpcDTO();
        BeanUtils.copyProperties(sysPlatformDO, sysPlatformRpcDTO);
        return sysPlatformRpcDTO;
    }

    public static WxPlatformRpcDTO toWxPlatformRpcDTO(WxPlatformDO wxPlatformDO) {
        WxPlatformRpcDTO wxPlatformRpcDTO = new WxPlatformRpcDTO();
        BeanUtils.copyProperties(wxPlatformDO, wxPlatformRpcDTO);
        return wxPlatformRpcDTO;
    }

    public static List<WxPlatformRpcDTO> toWxPlatformRpcDTOList(List<WxPlatformDO> list) {
        List<WxPlatformRpcDTO> resultWxPlatformRpcDTOS = new ArrayList<>();
        list.stream().forEach(wxPlatformDO -> {
            resultWxPlatformRpcDTOS.add(toWxPlatformRpcDTO(wxPlatformDO));
        });
        return resultWxPlatformRpcDTOS;
    }

    public static List<PlatformRpcDTO> toPlatformRpcDTOList(List<PlatformDO> platformDOS) {
        List<PlatformRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(platformDOS)) {
            platformDOS.forEach(platformDO -> {
                PlatformRpcDTO rpcDTO = new PlatformRpcDTO();
                BeanUtils.copyProperties(platformDO, rpcDTO);
                rpcDTOS.add(rpcDTO);
            });
        }
        return rpcDTOS;
    }
}
