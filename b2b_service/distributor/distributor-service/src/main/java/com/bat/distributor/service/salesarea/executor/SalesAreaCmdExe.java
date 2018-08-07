package com.bat.distributor.service.salesarea.executor;

import static com.bat.distributor.service.common.Constant.OPEN_NO;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.salesarea.dto.SalesAreaCmd;
import com.bat.distributor.api.salesarea.dto.SalesAreaId;
import com.bat.distributor.api.salesarea.dto.SalesAreaOpenCmd;
import com.bat.distributor.dao.salesarea.SalesAreaMapper;
import com.bat.distributor.dao.salesarea.dataobject.SalesAreaDO;
import com.bat.distributor.service.salesarea.convertor.SalesAreaConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SalesAreaCmdExe {

    @Resource
    private SalesAreaMapper mapper;

    /**
     * 创建分销商销售区域
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSalesArea(SalesAreaCmd cmd) {
        SalesAreaDO salesAreaDO = SalesAreaConvertor.toSalesAreaDo(cmd);
        mapper.insert(salesAreaDO);
    }

    /**
     * 更新分销商销售区域
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSalesArea(SalesAreaCmd cmd) {
        SalesAreaDO salesAreaDO = SalesAreaConvertor.toSalesAreaDo(cmd);
        mapper.updateByPrimaryKey(salesAreaDO);
    }

    /**
     * 更新分销商销售区域状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openSalesArea(SalesAreaOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(OPEN_NO)) {
            // 停用分销商销售区域前，确保分销商销售区域下的分销商都已转移或删除
            Integer count = mapper.getSalesAreaDistributorsCount(cmd.getId());
            if (count > 0) {
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_SALES_AREA_DISTRIBUTORNOTNUL);
            }
            // TODO 停用分销商销售区域前，确保分销商销售区域对应的仓库都已转移或删除
        }
        mapper.openSalesArea(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商角色
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSalesArea(SalesAreaId cmd) {
        SalesAreaDO salesAreaDO = mapper.selectByPrimaryKey(cmd.getId());
        if (salesAreaDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_SALES_AREA__NULL);
        }
        // 停用的分销商类别才允许删除
        if (!salesAreaDO.getOpenFlag().equals(OPEN_NO)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_SALES_AREA_DELETE_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
