package com.bat.flexible.manager.label;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.label.LabelDistributorRelevanceServiceI;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.label.executor.LabelDistributorRelevanceCmdExe;
import com.bat.flexible.manager.label.executor.LabelDistributorRelevanceQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.dao.label.dataobject.LabelDistributorRelevanceDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class LabelDistributorRelevanceServiceImpl implements LabelDistributorRelevanceServiceI {


    @Autowired
    private LabelDistributorRelevanceCmdExe labelDistributorRelevanceCmdExe;

    @Autowired
    private LabelDistributorRelevanceQryExe labelDistributorRelevanceQryExe;

    @Resource
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @DubboReference(check = false,retries = 0,timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public void saveRela(List<Integer> distributorIdList, Integer labelId, AdminResponse currentAdmin, boolean isAdd) {
        //编辑
        dealWithByUpload(distributorIdList,labelId,currentAdmin,isAdd);
        if(distributorIdList !=null && distributorIdList.size()>0){
            distributorIdList.stream().forEach(distributorId -> {
                LabelDistributorRelevanceDO relevanceDO = new LabelDistributorRelevanceDO();
                relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                relevanceDO.setLabelId(labelId);
                relevanceDO.setDistributorId(distributorId);
                setAdminMsg(relevanceDO,currentAdmin);
                labelDistributorRelevanceCmdExe.create(relevanceDO);
            });
        }
    }

    @Override
    public List<DistributorSimpleRelaQry> listDTOByCondition(Integer labelId, Integer distributorId) {
        List<LabelDistributorRelevanceDO> relevanceDOList = labelDistributorRelevanceQryExe.listByLabelIdAndDistributorId(labelId,distributorId);
        if (relevanceDOList ==null || relevanceDOList.size()==0){
            return  null;
        }
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        Map<Integer, DistributorRpcDTO> map = new HashMap<>();
        relevanceDOList.stream().forEach(labelDistributorRelevanceDO -> {
            DistributorSimpleRelaQry simpleRelaQry = new DistributorSimpleRelaQry();
            simpleRelaQry.setId(labelDistributorRelevanceDO.getId());
            simpleRelaQry.setDistributorId(labelDistributorRelevanceDO.getDistributorId());
            flexibleDistributorQryExe.setDistributorNameMsg(simpleRelaQry,map);
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    private void dealWithByUpload(List<Integer> distributorIdList, Integer labelId, AdminResponse currentAdmin, boolean isAdd) {
        if(isAdd){
            return;
        }
        List<LabelDistributorRelevanceDO> relevanceDOList = labelDistributorRelevanceQryExe.listByLabelIdAndDistributorId(labelId,null);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            for(int x=0;x<relevanceDOList.size();x++){
                if(distributorIdList !=null && distributorIdList.size()>0){
                    for(int y=0;y<distributorIdList.size();y++){
                        if(relevanceDOList.get(x).getDistributorId() - distributorIdList.get(y)==0){
                            relevanceDOList.remove(x);
                            x--;
                            distributorIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                relevanceDOList.stream().forEach(labelDistributorRelevanceDO -> {
                    labelDistributorRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    setAdminMsg(labelDistributorRelevanceDO,currentAdmin);
                    labelDistributorRelevanceCmdExe.update(labelDistributorRelevanceDO);
                });
            }
        }
    }

    private void setAdminMsg(LabelDistributorRelevanceDO labelDistributorRelevanceDO, AdminResponse currentAdmin) {
        if(labelDistributorRelevanceDO.getId() ==null){
            labelDistributorRelevanceDO.setCreateTime(new Date());
            labelDistributorRelevanceDO.setCreateUserId(currentAdmin.getId());
            labelDistributorRelevanceDO.setCreateUserName(currentAdmin.getUserName());
        }
        labelDistributorRelevanceDO.setUpdateTime(new Date());
        labelDistributorRelevanceDO.setUpdateUserId(currentAdmin.getId());
        labelDistributorRelevanceDO.setUpdateUserName(currentAdmin.getUserName());
    }
}
