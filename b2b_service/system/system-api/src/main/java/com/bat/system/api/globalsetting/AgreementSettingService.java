package com.bat.system.api.globalsetting;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.globalsetting.dto.*;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.api.globalsetting.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 11:22
 */
public interface AgreementSettingService {
    /**
     * 创建协议设置
     * 
     * @param cmd
     * @return
     */
    boolean createAgreementSetting(AgreementSettingCreateCmd cmd);

    /**
     * 根据Id获取协议设置
     * 
     * @param id
     * @return
     */
    AgreementSettingDTO getAgreementSettingById(Integer id);

    /**
     * 根据Id获取协议设置
     *
     * @param id
     * @return
     */
    AgreementSettingDTO getAgreementSettingByBrandId(AgreementSettingBrandQry id);

    /**
     * 获取协议设置列表
     * 
     * @param qry
     * @return
     */
    PageInfo<AgreementSettingDTO> listAgreementSetting(AgreementSettingQry qry);

    /**
     * 通过id删除协议
     * 
     * @param id
     * @return
     */
    boolean deleteAgreementSettingById(Integer id);

    /**
     * 更新协议
     * 
     * @param cmd
     * @return
     */
    boolean updateAgreementSetting(AgreementSettingUpdateCmd cmd);

    /**
     * 签署协议
     *
     * @return
     */
    boolean signAgreement(AgreementDistributorCmd cmd);

    /**
     * 获取协议签署状态
     *
     * @return
     */
    boolean getAgreementSignStatus(AgreementDistributorCmd cmd);

    /**
     * 检查品牌的绑定状态
     * 
     * @param qry
     * @return
     */
    List<Integer> checkBrandAgreementSetting(CheckBrandAgreementQry qry);

    /**
     * 获取国内用户协议
     * 
     * @return
     */
    AgreementSettingDTO getChinaUserAgreementSetting();

    /**
     * 获取用户已经签署的协议
     * @param distributorId
     * @return
     */
    List<AgreementSettingDTO> getAgreementSettingByDistributorId(Integer distributorId);
}
