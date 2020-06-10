package com.bat.flexible.manager.index;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.DistributorIndexRecommendRelevanceServiceI;
import com.bat.flexible.api.index.dto.page.DistributorIndexRecommendPageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.index.executor.DistributorIndexRecommendRelevanceCmdExe;
import com.bat.flexible.manager.index.executor.DistributorIndexRecommendRelevanceQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.flexible.dao.index.co.IndexRecommendPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorIndexRecommendRelevanceServiceImpl implements DistributorIndexRecommendRelevanceServiceI {

    @Autowired
    private DistributorIndexRecommendRelevanceCmdExe distributorIndexRecommendRelevanceCmdExe;

    @Autowired
    private DistributorIndexRecommendRelevanceQryExe distributorIndexRecommendRelevanceQryExe;

    @DubboReference(check = false,timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public void add(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer indexRecommendId, AdminResponse currentAdmin) {
        distributorSimpleRelaQryList.stream().forEach(distributorSimpleRelaQry -> {
            DistributorIndexRecommendRelevanceDO rela = new DistributorIndexRecommendRelevanceDO();
            rela.setCreateTime(new Date());
            rela.setIndexRecommendId(indexRecommendId);
            rela.setCreateUserId(currentAdmin.getId());
            rela.setCreateUserName(currentAdmin.getUserName());
            rela.setDistributorId(distributorSimpleRelaQry.getDistributorId());
            rela.setCompanyName(distributorSimpleRelaQry.getDistributorCompanyName());
            rela.setDistributorName(distributorSimpleRelaQry.getDistributorName());
            distributorIndexRecommendRelevanceCmdExe.create(rela);
        });
    }

    @Override
    public void save(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer indexRecommendId, AdminResponse currentAdmin) {
        List<DistributorIndexRecommendRelevanceDO> relaList = distributorIndexRecommendRelevanceQryExe.listByIndexRecommendId(indexRecommendId);
        //判断新增
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
            relaList.stream().forEach(recommendRela -> {
                distributorIndexRecommendRelevanceCmdExe.deleteById(recommendRela.getId());
            });
        }
        if(distributorSimpleRelaQryList !=null && distributorSimpleRelaQryList.size()>0){
            add(distributorSimpleRelaQryList,indexRecommendId,currentAdmin);
        }
    }

    @Override
    public void deleteByIndexRecommendId(Integer indexRecommendId) {
        distributorIndexRecommendRelevanceCmdExe.deleteByIndexRecommendId(indexRecommendId);
    }

    @Override
    public List<DistributorIndexRecommendRelevanceDO> listByDistributorIdList(List<Integer> distributorIdList) {
        return distributorIndexRecommendRelevanceQryExe.listByDistributorIdList(distributorIdList);
    }

    @Override
    public List<DistributorSimpleRelaQry> listDistributorMsg(Integer indexRecommendId) {
        List<DistributorIndexRecommendRelevanceDO> relevanceDOList = distributorIndexRecommendRelevanceQryExe.listByIndexRecommendId(indexRecommendId);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return null;
        }
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        relevanceDOList.stream().forEach(distributorIndexRecommendRelevanceDO -> {
            DistributorSimpleRelaQry simpleRelaQry = BeanUtils.copy(distributorIndexRecommendRelevanceDO,DistributorSimpleRelaQry.class);
            simpleRelaQry.setDistributorCompanyName(distributorIndexRecommendRelevanceDO.getCompanyName());
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    @Override
    public PageInfo<IndexRecommendPageCO> page(DistributorIndexRecommendPageQry distributorIndexRecommendPageQry) {
        PageHelper.startPage(distributorIndexRecommendPageQry.getPage(),distributorIndexRecommendPageQry.getSize());
        List<IndexRecommendPageCO> list = distributorIndexRecommendRelevanceQryExe.listCOByContent(distributorIndexRecommendPageQry.getContent());
        return new PageInfo(list);
    }
}
