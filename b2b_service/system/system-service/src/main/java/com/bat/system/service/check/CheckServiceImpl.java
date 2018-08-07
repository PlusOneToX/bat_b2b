package com.bat.system.service.check;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.check.CheckService;
import com.bat.system.api.check.dto.CheckConfigUpdateCmd;
import com.bat.system.api.check.dto.data.CheckConfigDTO;
import com.bat.system.service.check.executor.CheckCmdExc;
import com.bat.system.service.check.executor.CheckQryExc;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/15 15:29
 */
@Service
@Slf4j
public class CheckServiceImpl implements CheckService {

    @Resource
    private CheckQryExc checkQryExc;

    @Resource
    private CheckCmdExc checkCmdExc;

    @Override
    public List<CheckConfigDTO> checkDetail() {
        return checkQryExc.checkDetail(null);
    }

    @Override
    public void editCheckConfig(CheckConfigUpdateCmd cmd) {
        checkCmdExc.editCheckConfig(cmd);
    }
}
