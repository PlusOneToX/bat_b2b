package com.bat.system.api.storesetting;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.ColumnCreateCmd;
import com.bat.system.api.storesetting.dto.ColumnQry;
import com.bat.system.api.storesetting.dto.ColumnReleaseCmd;
import com.bat.system.api.storesetting.dto.ColumnUpdateCmd;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:01
 */
public interface ColumnService {
    /**
     * 创建栏目
     *
     * @param cmd
     * @return
     */
    boolean createColumn(ColumnCreateCmd cmd);

    /**
     * 更新栏目
     *
     * @param cmd
     * @return
     */
    boolean updateColumn(ColumnUpdateCmd cmd);

    /**
     * 发布栏目
     *
     * @param cmd
     * @return
     */
    boolean releaseColumn(ColumnReleaseCmd cmd);

    /**
     * 删除栏目
     *
     * @param id
     * @return
     */
    boolean deleteColumnById(Integer id);

    /**
     * 获取栏目列表
     *
     * @param qry
     * @return
     */
    PageInfo<ColumnDTO> listColumn(ColumnQry qry);

    /**
     * 通过id 获取栏目
     *
     * @param id
     * @return
     */
    ColumnDTO getColumn(Integer id);

    /**
     * 一键导入清仓商品（栏目）
     * 
     * @param id
     */
    void clearanceGoodsStoreColumn(Integer id);

    /**
     * 一键清除商品（栏目）
     * 
     * @param id
     */
    void clearGoodsStoreColumn(Integer id);
}
