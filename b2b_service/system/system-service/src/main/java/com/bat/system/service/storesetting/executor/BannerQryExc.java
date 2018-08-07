package com.bat.system.service.storesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.dto.BannerQry;
import com.bat.system.api.storesetting.dto.data.BannerDTO;
import com.bat.system.dao.storesetting.BannerMapper;
import com.bat.system.dao.storesetting.dataobject.BannerDO;
import com.bat.system.service.storesetting.convertor.BannerConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class BannerQryExc {
    @Resource
    private BannerMapper bannerMapper;

    public PageInfo<BannerDTO> listBanner(BannerQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<BannerDO> bannerDOS = bannerMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(bannerDOS);
        List<BannerDTO> toRoleDTOList = BannerConvertor.toBannerDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }
}
