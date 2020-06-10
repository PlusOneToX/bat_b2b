package com.bat.flexible.manager.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.picture.PictureDistributorRelevanceServiceI;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.picture.executor.PictureDistributorRelevanceCmdExe;
import com.bat.flexible.manager.picture.executor.PictureDistributorRelevanceQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.dao.picture.dataobject.PictureDistributorRelevanceDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PictureDistributorRelevanceServiceImpl implements PictureDistributorRelevanceServiceI {

    @Autowired
    private PictureDistributorRelevanceQryExe pictureDistributorRelevanceQryExe;

    @Autowired
    private PictureDistributorRelevanceCmdExe pictureDistributorRelevanceCmdExe;

    @Resource
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public void saveRela(Integer pictureId, List<Integer> distributorIdList, Boolean isAdd, AdminResponse adminResponse, Short resellerScope) {
        //判断分销商范围是否是指定分销商
        if(resellerScope - PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_PERSONAL_ASSIGN !=0){
            return;
        }
        //判断分销商id、需要redis
        //判断修改是否删掉了关联关系
        dealWithByUpdate(isAdd,pictureId,distributorIdList,adminResponse);
        if(distributorIdList !=null && distributorIdList.size()>0){
            distributorIdList.stream().forEach(distributorId->{
                PictureDistributorRelevanceDO relevanceDO = new PictureDistributorRelevanceDO();
                relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                relevanceDO.setPictureId(pictureId);
                relevanceDO.setDistributorId(distributorId);
                setAdminMsg(relevanceDO,adminResponse);
                pictureDistributorRelevanceCmdExe.create(relevanceDO);
            });
        }
    }

    @Override
    public List<DistributorSimpleRelaQry> listByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        List<PictureDistributorRelevanceDO> relevanceDOList = pictureDistributorRelevanceQryExe.listByPictureIdAAndDelFlag(pictureId,delFlag);
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        Map<Integer, DistributorRpcDTO> map = new HashMap<>();
        relevanceDOList.stream().forEach(pictureDistributorRelevanceDO -> {
            DistributorSimpleRelaQry simpleRelaQry = new DistributorSimpleRelaQry();
            simpleRelaQry.setId(pictureDistributorRelevanceDO.getId());
            simpleRelaQry.setDistributorId(pictureDistributorRelevanceDO.getDistributorId());
            //redis取数据
            flexibleDistributorQryExe.setDistributorNameMsg(simpleRelaQry,map);
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    @Override
    public List<PictureDistributorRelevanceDO> listByCondition(Integer pictureId, List<Integer> distributorIds) {
        return pictureDistributorRelevanceQryExe.listByCondition(pictureId,distributorIds);
    }

    private void dealWithByUpdate(Boolean isAdd, Integer pictureId, List<Integer> distributorIdList, AdminResponse adminResponse) {
        if(isAdd){
            return;
        }
        List<PictureDistributorRelevanceDO> relevanceDOList = pictureDistributorRelevanceQryExe.listByPictureIdAAndDelFlag(pictureId,FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            for(int x=0;x<relevanceDOList.size();x++){
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
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                relevanceDOList.stream().forEach(pictureDistributorRelevanceDO -> {
                    setAdminMsg(pictureDistributorRelevanceDO,adminResponse);
                    pictureDistributorRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    pictureDistributorRelevanceCmdExe.update(pictureDistributorRelevanceDO);
                });
            }
        }
    }

    private void setAdminMsg(PictureDistributorRelevanceDO relevanceDO, AdminResponse adminResponse) {
        if(relevanceDO.getId()==null){
            relevanceDO.setCreateTime(new Date());
            relevanceDO.setCreateUserId(adminResponse.getId());
            relevanceDO.setCreateUserName(adminResponse.getUserName());
        }
        relevanceDO.setUpdateTime(new Date());
        relevanceDO.setUpdateUserId(adminResponse.getId());
        relevanceDO.setUpdateUserName(adminResponse.getUserName());
    }
}
