package com.bat.system.api.logistics;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.logistics.dto.*;
import com.bat.system.api.logistics.dto.data.FormulaCheckDTO;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.api.logistics.dto.data.LogisticsQryDTO;
import com.bat.system.api.logistics.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface LogisticsService {
    /**
     * 添加配送
     *
     * @param cmd
     * @return
     */
    boolean createLogistics(LogisticsCreateCmd cmd);

    /**
     * 根据ID获取配送
     *
     * @param id
     * @return
     */
    LogisticsDTO getLogisticsById(Integer id);

    /**
     * 获取所有配送（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<LogisticsDTO> listLogistics(LogisticsQry qry);

    /**
     * 根据id删除配送
     *
     * @param id
     * @return
     */
    boolean deleteLogisticsById(Integer id);

    /**
     * 更新配送
     * 
     * @param cmd
     * @return
     */
    boolean updateLogistics(LogisticsUpdateCmd cmd);

    /**
     * 公式校验
     * 
     * @param cmd
     * @return
     */
    FormulaCheckDTO verificationDistributionFormula(FormulaCheckCmd cmd);

    /**
     * 配送费用计算
     * 
     * @param qry
     * @return
     */
    List<LogisticsQryDTO> listLogisticsCalculation(LogisticsCalculationQry qry);

    /**
     * 根据地区和购买商品获取配送方式和计算配送费用 列表
     * 
     * @param qrys
     * @return
     */
    List<LogisticsQryDTO> listLogisticsCalculations(List<LogisticsCalculationQry> qrys);

}
