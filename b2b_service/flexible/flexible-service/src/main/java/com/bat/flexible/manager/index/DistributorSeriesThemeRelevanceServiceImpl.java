package com.bat.flexible.manager.index;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.DistributorSeriesThemeRelevanceServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.index.executor.DistributorSeriesThemeRelevanceCmdExe;
import com.bat.flexible.manager.index.executor.DistributorSeriesThemeRelevanceQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorSeriesThemeRelevanceServiceImpl implements DistributorSeriesThemeRelevanceServiceI {

    @Autowired
    private DistributorSeriesThemeRelevanceCmdExe distributorSeriesThemeRelevanceCmdExe;

    @Autowired
    private DistributorSeriesThemeRelevanceQryExe distributorSeriesThemeRelevanceQryExe;

    @DubboReference(check = false,timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public void add(Integer seriesThemeId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin) {
        distributorSimpleRelaQryList.stream().forEach(distributorSimpleRelaQry -> {
            DistributorSeriesThemeRelevanceDO rela = new DistributorSeriesThemeRelevanceDO();
            rela.setCreateUserName(currentAdmin.getUserName());
            rela.setDistributorId(distributorSimpleRelaQry.getDistributorId());
            rela.setDistributorName(distributorSimpleRelaQry.getDistributorName());
            rela.setCompanyName(distributorSimpleRelaQry.getDistributorCompanyName());
            rela.setCreateTime(new Date());
            rela.setSeriesThemeId(seriesThemeId);
            rela.setCreateUserId(currentAdmin.getId());
            distributorSeriesThemeRelevanceCmdExe.create(rela);
        });
    }

    @Override
    public void save(Integer seriesThemeId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin) {
        List<DistributorSeriesThemeRelevanceDO> relaList = distributorSeriesThemeRelevanceQryExe.listBySeriesThemeId(seriesThemeId);
        for(int x=0;x<distributorSimpleRelaQryList.size();x++){
            for(int y=0;y<relaList.size();y++){
                if(distributorSimpleRelaQryList.get(x).getDistributorId() - relaList.get(y).getDistributorId() ==0){
                    distributorSimpleRelaQryList.remove(x);
                    relaList.remove(y);
                    x--;
                    y--;
                    break;
                }
            }
        }
        if(relaList !=null && relaList.size()>0){
            relaList.stream().forEach(distributorSeriesThemeRela -> {
                distributorSeriesThemeRelevanceCmdExe.deleteById(distributorSeriesThemeRela.getId());
            });
        }
        if(distributorSimpleRelaQryList !=null && distributorSimpleRelaQryList.size()>0){
            add(seriesThemeId,distributorSimpleRelaQryList,currentAdmin);
        }
    }

    @Override
    public List<DistributorSimpleRelaQry> listDistributorSimpleMsgBySeriesThemeId(Integer seiesThemeId) {
       // return distributorSeriesThemeRelaDataManager.listDistributorSimpleMsgBySeriesThemeId(seiesThemeId);
        List<DistributorSeriesThemeRelevanceDO> themeRelevanceDOList = distributorSeriesThemeRelevanceQryExe.listBySeriesThemeId(seiesThemeId);
        if(themeRelevanceDOList ==null || themeRelevanceDOList.size()==0){
            return null;
        }
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        themeRelevanceDOList.stream().forEach(distributorSeriesThemeRelevanceDO -> {
            DistributorSimpleRelaQry simpleRelaQry = BeanUtils.copy(distributorSeriesThemeRelevanceDO,DistributorSimpleRelaQry.class);
            simpleRelaQry.setDistributorCompanyName(distributorSeriesThemeRelevanceDO.getCompanyName());
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    @Override
    public void deleteBySeriesThemeId(Integer seriesThemeId) {
        distributorSeriesThemeRelevanceCmdExe.deleteBySeriesThemeId(seriesThemeId);
    }

    @Override
    public List<DistributorSeriesThemeRelevanceDO> findByDistributorIdList(List<Integer> distributorIdList, Integer seriesId) {
        return distributorSeriesThemeRelevanceQryExe.listByDistributorIdList(distributorIdList,seriesId);
    }
}
