package com.bat.order.service.cart;

import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.cart.executor.ShoppingCartCustomerCmdExe;
import com.bat.order.service.cart.executor.ShoppingCartCustomerQryExe;
import org.springframework.stereotype.Service;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.ShoppingCartCustomerServiceI;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.ShoppingCustomerCartCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;

@Service
public class ShoppingCartCustomerServiceImpl implements ShoppingCartCustomerServiceI {

    @Resource
    private ShoppingCartCustomerCmdExe customerCmdExe;
    @Resource
    private ShoppingCartCustomerQryExe customerQryExe;

    @Override
    public List<ShoppingCartDTO> list(String userId, String distributorId, String language) {
        return customerQryExe.list(userId, distributorId, language);
    }

    @Override
    public BaseId createDiy(ShoppingCustomerCartCmd cmd, String userId, String distributorId, String language) {
        return customerCmdExe.createDiy(cmd, userId, distributorId, language);
    }

    @Override
    public void createDiyList(List<ShoppingCustomerCartCmd> cmds, String userId, String distributorId,
        String language) {
        customerCmdExe.createDiyList(cmds, userId, distributorId, language);
    }

    @Override
    public BaseId create(ShoppingCartCmd cmd, String userId, String distributorId, String language) {
        return customerCmdExe.create(cmd, userId, distributorId, language);
    }

    @Override
    public void createList(List<ShoppingCartCmd> cmds, String userId, String distributorId, String language) {
        customerCmdExe.createList(cmds, userId, distributorId, language);
    }

    @Override
    public void update(ShoppingCartUpdateCmd cmd, String userId, String distributorId) {
        customerCmdExe.update(cmd, userId, distributorId);
    }

    @Override
    public void deleteByIds(BaseIds cmd) {
        customerCmdExe.deleteByIds(cmd);
    }

    @Override
    public void deleteById(BaseId cmd) {
        customerCmdExe.deleteById(cmd);
    }

}
