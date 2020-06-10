package com.bat.flexible.manager.distributor.exclude;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorPictureExcludeCmdExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorPictureExcludeQryExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.distributor.exclude.DistributorPictureExcludeServiceI;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DistributorPictureExcludeServiceImpl implements DistributorPictureExcludeServiceI {

    @Autowired
    private DistributorPictureExcludeCmdExe distributorPictureExcludeCmdExe;

    @Autowired
    private DistributorPictureExcludeQryExe distributorPictureExcludeQryExe;
    @Resource
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 保存分销商不适用列表
     * @param pictureId 图片id
     * @param distributorIdList 分销商id列表
     * @param isAdd 是否新增 true新增 false修改
     * @param adminResponse 后台操作人信息
     */
    @Override
    @Transactional
    public void save(Integer pictureId, List<Integer> distributorIdList, Boolean isAdd, AdminResponse adminResponse) {
        dealWithByUpdate(pictureId,distributorIdList,isAdd,adminResponse);
        if(distributorIdList !=null && distributorIdList.size()>0){
            distributorIdList.stream().forEach(distributorId -> {
                DistributorPictureExcludeDO excludeDO = new DistributorPictureExcludeDO();
                excludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                excludeDO.setPictureId(pictureId);
                excludeDO.setDistributorId(distributorId);
                setAdminMsg(excludeDO,adminResponse);
                distributorPictureExcludeCmdExe.create(excludeDO);
            });
        }
    }

    @Override
    public List<DistributorSimpleRelaQry> listByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        List<DistributorPictureExcludeDO> distributorPictureExcludeDOList = distributorPictureExcludeQryExe.listByPictureIdAndDelFlag(pictureId,delFlag);
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        Map<Integer, DistributorRpcDTO> map = new HashMap<>();
        distributorPictureExcludeDOList.stream().forEach(distributorPictureExcludeDO -> {
            DistributorSimpleRelaQry simpleRelaQry = new DistributorSimpleRelaQry();
            simpleRelaQry.setDistributorId(distributorPictureExcludeDO.getDistributorId());
            simpleRelaQry.setId(distributorPictureExcludeDO.getId());
            //设置redis信息
            flexibleDistributorQryExe.setDistributorNameMsg(simpleRelaQry,map);
            relaQryList.add(simpleRelaQry);
        });
        return relaQryList;
    }

    @Override
    public List<DistributorPictureExcludeDO> listByDistributorId(List<Integer> distributorIds) {
        return distributorPictureExcludeQryExe.listByDistributorId(distributorIds);
    }

    private void dealWithByUpdate(Integer pictureId, List<Integer> distributorIdList, Boolean isAdd, AdminResponse adminResponse) {
        if(isAdd){
            return;
        }
        List<DistributorPictureExcludeDO> distributorPictureExcludeDOList = distributorPictureExcludeQryExe.listByPictureIdAndDelFlag(pictureId, FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        if(distributorPictureExcludeDOList !=null && distributorPictureExcludeDOList.size()>0){
            for(int x=0;x<distributorPictureExcludeDOList.size();x++){
                if(distributorIdList!=null && distributorIdList.size()>0){
                    for(int y=0;y<distributorIdList.size();y++){
                        if(distributorPictureExcludeDOList.get(x).getDistributorId() - distributorIdList.get(y)==0){
                            distributorIdList.remove(y);
                            y--;
                            distributorPictureExcludeDOList.remove(x);
                            x--;
                            break;
                        }
                    }
                }
            }
            if(distributorPictureExcludeDOList !=null && distributorPictureExcludeDOList.size()>0){
                distributorPictureExcludeDOList.stream().forEach(distributorPictureExcludeDO -> {
                    setAdminMsg(distributorPictureExcludeDO,adminResponse);
                    distributorPictureExcludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    distributorPictureExcludeCmdExe.update(distributorPictureExcludeDO);
                });
            }
        }
    }

    private void setAdminMsg(DistributorPictureExcludeDO distributorPictureExcludeDO, AdminResponse adminResponse) {
        if(distributorPictureExcludeDO.getId() ==null){
            distributorPictureExcludeDO.setCreateTime(new Date());
            distributorPictureExcludeDO.setCreateUserId(adminResponse.getId());
            distributorPictureExcludeDO.setCreateUserName(adminResponse.getUserName());
        }
        distributorPictureExcludeDO.setUpdateTime(new Date());
        distributorPictureExcludeDO.setUpdateUserId(adminResponse.getId());
        distributorPictureExcludeDO.setUpdateUserName(adminResponse.getUserName());
    }
}
