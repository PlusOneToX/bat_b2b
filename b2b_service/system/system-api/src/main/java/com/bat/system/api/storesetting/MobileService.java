package com.bat.system.api.storesetting;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.MobileCreateCmd;
import com.bat.system.api.storesetting.dto.MobileQry;
import com.bat.system.api.storesetting.dto.MobileReleaseCmd;
import com.bat.system.api.storesetting.dto.MobileUpdateCmd;
import com.bat.system.api.storesetting.dto.data.MobileDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 15:01
 */
public interface MobileService {

    /**
     * 创建移动端
     * 
     * @param cmd
     * @return
     */
    boolean createMobile(MobileCreateCmd cmd);

    /**
     * 更新移动端
     * 
     * @param cmd
     * @return
     */
    boolean updateMobile(MobileUpdateCmd cmd);

    /**
     * 获取移动端通过类型
     * 
     * @param moduleType
     * @return
     */
    MobileDTO getMobileByModuleType(Short moduleType);

    /**
     * 发布移动端
     * 
     * @param cmd
     * @return
     */
    boolean releaseMobile(MobileReleaseCmd cmd);

    /**
     * 通过id删除移动端
     * 
     * @param id
     * @return
     */
    boolean deleteMobileById(Integer id);

    /**
     * 获取移动端列表
     * 
     * @param qry
     * @return
     */
    PageInfo<MobileDTO> listMobile(MobileQry qry);

    /**
     * 删除移动端item通过id
     * 
     * @param id
     * @return
     */
    boolean deleteMobileItemById(Integer id);

    /**
     * 通过id获取移动端
     * 
     * @param id
     * @return
     */
    MobileDTO getMobileById(Integer id);

}
