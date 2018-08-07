package com.bat.distributor.service.salesarea.executor;

import static com.bat.distributor.service.salesarea.executor.ErrorCode.B_DISTRIBUTOR_SALES_AREA__NULL;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.salesarea.dto.SalesAreaId;
import com.bat.distributor.api.salesarea.dto.SalesAreaListQry;
import com.bat.distributor.api.salesarea.dto.data.SalesAreaDTO;
import com.bat.distributor.dao.salesarea.SalesAreaMapper;
import com.bat.distributor.dao.salesarea.dataobject.SalesAreaDO;
import com.bat.distributor.service.salesarea.convertor.SalesAreaConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class SalesAreaQryExe {

    @Resource
    private SalesAreaMapper mapper;

    /**
     * 查询分销商销售区域列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<SalesAreaDTO> executeList(SalesAreaListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<SalesAreaDO> salesAreaDOList = mapper.listSalesArea(qryMap);
        PageInfo pageInfo = new PageInfo(salesAreaDOList);
        List<SalesAreaDTO> salesAreaDTOList = SalesAreaConvertor.toSalesAreaDTOList(pageInfo.getList());
        pageInfo.setList(salesAreaDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商销售区域ID查询分销商销售区域详情
     * 
     * @param qry
     * @return
     */
    public SalesAreaDTO execute(SalesAreaId qry) {
        SalesAreaDO salesAreaDO = mapper.selectByPrimaryKey(qry.getId());
        if (salesAreaDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_SALES_AREA__NULL);
        }
        SalesAreaDTO dto = SalesAreaConvertor.toSalesAreaDTO(salesAreaDO);
        return dto;
    }
}
