package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountRatioServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountSalemanServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;
import com.bat.distributor.dao.subaccount.co.SubAccountUserConfigCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountLevelQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountUserConfigQryExe;
import com.bat.distributor.service.common.DistributorCommonConstant;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountLevelCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorSubAccountLevelServiceImpl implements DistributorSubAccountLevelServiceI {

    @Autowired
    private DistributorSubAccountLevelCmdExe distributorSubAccountLevelCmdExe;

    @Autowired
    private DistributorSubAccountLevelQryExe distributorSubAccountLevelQryExe;

    @Autowired
    private DistributorSubAccountRatioServiceI distributorSubAccountRatioServiceI;

    @Autowired
    private DistributorSubAccountSalemanServiceI distributorSubAccountSalemanServiceI;

    @Autowired
    private DistributorSubAccountUserConfigQryExe distributorSubAccountUserConfigQryExe;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(boolean isAdd, Integer distributorId, List<String> levelNameList, String userId, String userName) {
        //处理编辑
        dealwithUpdate(isAdd,distributorId,levelNameList,userId,userName);
        for(int x=0;x<levelNameList.size();x++){
            String levelName = levelNameList.get(x);
            if(StringUtils.isBlank(levelName)){
                //空的表示修改置空
               continue;
            }
            DistributorSubAccountLevelDO levelDO = new DistributorSubAccountLevelDO();
            levelDO.setDistributorId(distributorId);
            levelDO.setLevelName(levelName);
            levelDO.setSequence(x+1);
            levelDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
            setAdminMsg(levelDO,userId,userName);
            distributorSubAccountLevelCmdExe.create(levelDO);
            // 新增等级的情况下 该分销商添加 分账比例 为0
            List<SubAccountUserConfigCO> configCOS = distributorSubAccountUserConfigQryExe.listCOByDistributorId(distributorId + "");
            configCOS.forEach(subAccountUserConfigCO -> {
                List<SubAccountLevelRatioDTO> subAccountLevelRatioDTOS = new ArrayList<>();
                SubAccountLevelRatioDTO subAccountLevelRatioDTO = new SubAccountLevelRatioDTO();
                subAccountLevelRatioDTO.setLevelId(levelDO.getId());
                subAccountLevelRatioDTO.setLevelName(levelName);
                subAccountLevelRatioDTO.setRatio(BigDecimal.ZERO);
                subAccountLevelRatioDTOS.add(subAccountLevelRatioDTO);
                distributorSubAccountRatioServiceI.create(subAccountUserConfigCO.getId(),userId,userName,subAccountLevelRatioDTOS);
            });
        }
    }

    @Override
    public List<DistributorSubAccountLevelDO> listByDistributorId(Integer distributorId) {
        return distributorSubAccountLevelQryExe.listByDistributorId(distributorId);
    }

    /**
     * 处理修改
     * @param isAdd
     * @param distributorId
     * @param levelNameList
     * @param userId
     * @param userName
     */
    private void dealwithUpdate(boolean isAdd, Integer distributorId, List<String> levelNameList, String userId, String userName) {
        if(isAdd){
            return;
        }
        List<DistributorSubAccountLevelDO> levelDOList = distributorSubAccountLevelQryExe.listByDistributorId(distributorId);

        for(int x=0 ; x < levelDOList.size() ; x++){
            DistributorSubAccountLevelDO accountLevelDO = levelDOList.get(x);
            if(levelNameList.size()>levelDOList.size()){
                accountLevelDO.setLevelName(levelNameList.get(x));
                //设置为空
                levelNameList.set(x,null);
            }else{
                //修改后等级长度小于之前的列表长度
                if(x > levelNameList.size()-1){
                    //删除
                    accountLevelDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_YES);
                }else{
                    accountLevelDO.setLevelName(levelNameList.get(x));
                    //设置为空
                    levelNameList.set(x,null);
                }
            }
            setAdminMsg(accountLevelDO,userId,userName);
            distributorSubAccountLevelCmdExe.update(accountLevelDO);
            if(DistributorCommonConstant.COMMON_DELETE_FLAG_YES.equals(accountLevelDO.getDeleteFlag())){
                //删除等级
                distributorSubAccountRatioServiceI.deleteByLevelId(accountLevelDO.getId(),Integer.parseInt(userId),userName);
                //业务员设置等级为空
                distributorSubAccountSalemanServiceI.updateLevelIdNull(accountLevelDO.getId(),Integer.parseInt(userId),userName);
            }
        }

    }

    private void setAdminMsg(DistributorSubAccountLevelDO accountLevelDO, String userId, String userName) {
        if(accountLevelDO.getId() ==null){
            accountLevelDO.setCreateTime(new Date());
            accountLevelDO.setCreateUserId(Integer.parseInt(userId));
            accountLevelDO.setCreateUserName(userName);
        }
        accountLevelDO.setUpdateTime(new Date());
        accountLevelDO.setUpdateUserId(Integer.parseInt(userId));
        accountLevelDO.setUpdateUserName(userName);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.set(0,null);
        System.out.println(list.size());
    }
}
