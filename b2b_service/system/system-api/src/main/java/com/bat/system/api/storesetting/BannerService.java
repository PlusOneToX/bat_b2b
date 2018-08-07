package com.bat.system.api.storesetting;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.BannerCreateCmd;
import com.bat.system.api.storesetting.dto.BannerQry;
import com.bat.system.api.storesetting.dto.BannerUpdateCmd;
import com.bat.system.api.storesetting.dto.data.BannerDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:11
 */
public interface BannerService {
    /**
     * 获取推广列表
     * 
     * @param qry
     * @return
     */
    PageInfo<BannerDTO> listBanner(BannerQry qry);

    /**
     * 创建推广
     * 
     * @param cmd
     * @return
     */
    boolean createBanner(BannerCreateCmd cmd);

    /**
     * 更新推广
     * 
     * @param cmd
     * @return
     */
    boolean updateBanner(BannerUpdateCmd cmd);

    /**
     * 通过id删除推广
     * 
     * @param id
     * @return
     */
    boolean deleteBannerById(Integer id);

    /**
     * 推广上移
     * 
     * @param id
     * @return
     */
    boolean sortBannerUp(Integer id);

    /**
     * 推广下移
     * 
     * @param id
     * @return
     */
    boolean sortBannerDown(Integer id);
}
