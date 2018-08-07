package com.bat.order.api.cart;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.ShoppingCustomerCartCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;

import java.util.List;

public interface ShoppingCartCustomerServiceI {
    /**
     * 获取购物车商品列表
     * 
     * @param userId
     * @return
     */
    List<ShoppingCartDTO> list(String userId, String distributorId, String language);

    /**
     * 新增购物车商品(支持C端柔性定制)
     *
     * @param cmd
     * @param userId
     */
    BaseId createDiy(ShoppingCustomerCartCmd cmd, String userId, String distributorId, String language);

    /**
     * 批量加入购物车商品(支持C端柔性定制)
     *
     * @param cmds
     * @param userId
     * @param distributorId
     */
    void createDiyList(List<ShoppingCustomerCartCmd> cmds, String userId, String distributorId, String language);

    /**
     * 新增购物车商品
     * 
     * @param cmd
     * @param userId
     */
    BaseId create(ShoppingCartCmd cmd, String userId, String distributorId, String language);

    /**
     * 批量加入购物车商品
     * 
     * @param cmds
     * @param userId
     * @param distributorId
     */
    void createList(List<ShoppingCartCmd> cmds, String userId, String distributorId, String language);

    /**
     * 修改购物车商品
     * 
     * @param cmd
     */
    void update(ShoppingCartUpdateCmd cmd, String userId, String distributorId);

    /**
     * 批量删除购物车
     * 
     * @param cmd
     */
    void deleteByIds(BaseIds cmd);

    /**
     * 删除购物车
     * 
     * @param cmd
     */
    void deleteById(BaseId cmd);

}
