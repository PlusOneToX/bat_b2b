package com.bat.flexible.manager.index;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.DistributorBannerRelevanceServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.index.executor.DistributorBannerRelevanceQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.flexible.dao.index.dataobject.DistributorBannerRelevanceDO;
import com.bat.flexible.manager.index.executor.DistributorBannerRelevanceCmdExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorBannerRelevanceServiceImpl implements DistributorBannerRelevanceServiceI {



    @Autowired
    private DistributorBannerRelevanceCmdExe distributorBannerRelevanceCmdExe;

    @Autowired
    private DistributorBannerRelevanceQryExe distributorBannerRelevanceQryExe;

    @DubboReference(check = false,timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public void add(Integer bannerId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin) {
        distributorSimpleRelaQryList.stream().forEach(distributorSimpleRelaQry -> {
            DistributorBannerRelevanceDO rela = new DistributorBannerRelevanceDO();
            rela.setBannerId(bannerId);
            rela.setCreateTime(new Date());
            rela.setCreateUserName(currentAdmin.getUserName());
            rela.setCreateUserId(currentAdmin.getId());
            rela.setDistributorId(distributorSimpleRelaQry.getDistributorId());
            rela.setCompanyName(distributorSimpleRelaQry.getDistributorCompanyName());
            rela.setDistributorName(distributorSimpleRelaQry.getDistributorName());
            distributorBannerRelevanceCmdExe.create(rela);
        });
    }

    @Override
    public void save(Integer bannerId, List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, AdminResponse currentAdmin) {
        List<DistributorBannerRelevanceDO> relaList = distributorBannerRelevanceQryExe.listByBannerId(bannerId);
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
            relaList.stream().forEach(distributorBannerRela -> {
                distributorBannerRelevanceCmdExe.deleteById(distributorBannerRela.getId());
            });
        }
        if(distributorSimpleRelaQryList !=null && distributorSimpleRelaQryList.size()>0){
            add(bannerId,distributorSimpleRelaQryList,currentAdmin);
        }
    }

    @Override
    public List<DistributorSimpleRelaQry> listDistributorMsgByBannerId(Integer bannerId) {
        List<DistributorBannerRelevanceDO> relevanceDOList = distributorBannerRelevanceQryExe.listByBannerId(bannerId);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return null;
        }
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        relevanceDOList.stream().forEach(distributorBannerRelevanceDO -> {
            DistributorSimpleRelaQry simpleRelaQry = BeanUtils.copy(distributorBannerRelevanceDO,DistributorSimpleRelaQry.class);
            simpleRelaQry.setDistributorCompanyName(distributorBannerRelevanceDO.getCompanyName());
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    @Override
    public void deleteByBannerIdList(List<Integer> bannerIdList) {
        distributorBannerRelevanceCmdExe.deleteByBannerIdList(bannerIdList);
    }
}
