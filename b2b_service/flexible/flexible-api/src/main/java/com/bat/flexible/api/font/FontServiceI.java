package com.bat.flexible.api.font;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.font.dto.FontCmd;
import com.bat.flexible.api.font.dto.FontPageQry;
import com.bat.flexible.dao.font.co.FontCO;

import java.util.List;

public interface FontServiceI {
    /**
     *
     * @param fontCmd
     * @param currentAdmin
     * @return
     */
    Response create(FontCmd fontCmd, AdminResponse currentAdmin);

    /**
     * 分页查询
     * @param fontPageQry
     * @return
     */
    PageInfo<FontCO> page(FontPageQry fontPageQry);

    /**
     * 修改
     * @param fontCmd
     * @param currentAdmin
     * @return
     */
    Response update(FontCmd fontCmd, AdminResponse currentAdmin);

    /**
     * 字体详情
     * @param id
     * @return
     */
    FontCmd detailById(Integer id);

    /**
     * 上下移动
     * @param flexibleUpOrDownDTO
     * @param currentAdmin
     * @return
     */
    Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    /**
     * 删除
     * @param id
     * @param currentAdmin
     * @return
     */
    Response deleteById(Integer id, AdminResponse currentAdmin);

    /**
     * 启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    /**
     * 列表查询可用字体
     * @return
     */
    List<FontCO> listUsable();
}
