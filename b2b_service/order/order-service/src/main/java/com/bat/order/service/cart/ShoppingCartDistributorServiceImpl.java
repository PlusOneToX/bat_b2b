package com.bat.order.service.cart;

import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.cart.executor.ShoppingCartDistributorQryExe;
import org.springframework.stereotype.Service;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.ShoppingCartDistributorServiceI;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;
import com.bat.order.service.cart.executor.ShoppingCartDistributorCmdExe;

@Service
public class ShoppingCartDistributorServiceImpl implements ShoppingCartDistributorServiceI {

    @Resource
    private ShoppingCartDistributorCmdExe distributorCmdExe;

    @Resource
    private ShoppingCartDistributorQryExe distributorQryExe;

    @Override
    public List<ShoppingCartDTO> list(String userId, String language) {
        return distributorQryExe.list(userId, language);
    }

    @Override
    public void createList(List<ShoppingCartCmd> cmds, String userId, String language) {
        distributorCmdExe.createList(cmds, userId, language);
    }

    @Override
    public BaseId create(ShoppingCartCmd cmd, String userId, String language) {
        return distributorCmdExe.create(cmd, userId, language);
    }

    @Override
    public void update(ShoppingCartUpdateCmd cmd, String userId) {
        distributorCmdExe.update(cmd, userId);
    }

    @Override
    public void deleteByIds(BaseIds cmd) {
        distributorCmdExe.deleteByIds(cmd);
    }

    @Override
    public void deleteById(BaseId cmd) {
        distributorCmdExe.deleteById(cmd);
    }
}
