package com.bat.system.api.check;

import java.util.List;

import com.bat.system.api.check.dto.CheckConfigUpdateCmd;
import com.bat.system.api.check.dto.data.CheckConfigDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface CheckService {

    List<CheckConfigDTO> checkDetail();

    void editCheckConfig(CheckConfigUpdateCmd cmd);
}
