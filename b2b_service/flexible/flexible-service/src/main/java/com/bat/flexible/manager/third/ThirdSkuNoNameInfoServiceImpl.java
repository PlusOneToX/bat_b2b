package com.bat.flexible.manager.third;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.third.ThirdSkuNoNameInfoServiceI;
import com.bat.flexible.manager.common.constant.third.ThirdSkuNoNameInfoConstant;
import com.bat.flexible.manager.third.executor.ThirdSkuNoNameInfoCmdExe;
import com.bat.flexible.manager.third.executor.ThirdSkuNoNameInfoQryExe;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class ThirdSkuNoNameInfoServiceImpl implements ThirdSkuNoNameInfoServiceI {

    @Autowired
    private ThirdSkuNoNameInfoQryExe thirdSkuNoNameInfoQryExe;

    @Autowired
    private ThirdSkuNoNameInfoCmdExe thirdSkuNoNameInfoCmdExe;

    @Override
    public List<ThirdSkuNoNameInfoDO> listByDistributorId(Integer distributorId) {
        return thirdSkuNoNameInfoQryExe.listByDistributorId(distributorId);
    }

    @Override
    public void deleteByDistributorId(Integer distributorId) {
        thirdSkuNoNameInfoCmdExe.deleteByDistributorId(distributorId);
    }

    @Override
    @Transactional
    public void add(Map<String, String> materialCategoryMap, Map<String, String> brandMap, Map<String, String> thirdModelMap, Map<String, String> thirdMaterialMap, Map<String, String> colorMap, AdminResponse currentAdmin, Integer distributorId) {
        addByMap(materialCategoryMap, currentAdmin, distributorId, ThirdSkuNoNameInfoConstant.TYPE_SKU_CATEGORY);
        addByMap(brandMap, currentAdmin, distributorId,ThirdSkuNoNameInfoConstant.TYPE_BRAND);
        addByMap(thirdModelMap, currentAdmin, distributorId,ThirdSkuNoNameInfoConstant.TYPE_MODEL);
        addByMap(thirdMaterialMap, currentAdmin, distributorId,ThirdSkuNoNameInfoConstant.TYPE_MATERIAL);
        addByMap(colorMap, currentAdmin, distributorId,ThirdSkuNoNameInfoConstant.TYPE_COLOUR);
    }

    @Override
    public void create(ThirdSkuNoNameInfoDO thirdSkuNoNameInfo) {
        thirdSkuNoNameInfoCmdExe.create(thirdSkuNoNameInfo);
    }


    private void addByMap(Map<String, String> materialCategoryMap, AdminResponse currentAdmin, Integer distributorId, Short type) {
        Iterator<Map.Entry<String, String>> iterator = materialCategoryMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            String no = entry.getKey();
            String name = entry.getValue();
            String extName = null;
            if(type - ThirdSkuNoNameInfoConstant.TYPE_MODEL ==0){
                String[] arr = name.split("_");
                name = arr[0];
                extName=arr[1];
            }
            saveInfo(currentAdmin, distributorId, no,name,extName, type);
        }
    }

    private void saveInfo(AdminResponse currentAdmin, Integer distributorId, String thirdNo,String thirdName,String extName,Short type) {
        ThirdSkuNoNameInfoDO info = new ThirdSkuNoNameInfoDO();
        info.setCreateTime(new Date());
        info.setCreateUserId(currentAdmin.getId());
        info.setCreateUserName(currentAdmin.getUserName());
        info.setUpdateUserId(currentAdmin.getId());
        info.setUpdateUserName(currentAdmin.getUserName());
        info.setUpdateTime(new Date());
        info.setDistributorId(distributorId);
        info.setThirdNo(thirdNo);
        info.setThirdName(thirdName);
        info.setThirdExtName(extName);
        info.setType(type);
        thirdSkuNoNameInfoCmdExe.create(info);
    }
}
