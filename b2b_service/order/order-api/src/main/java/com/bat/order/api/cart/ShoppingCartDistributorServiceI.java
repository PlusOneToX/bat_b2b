package com.bat.order.api.cart;

import java.util.List;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;

public interface ShoppingCartDistributorServiceI {
    /**
     * 获取购物车商品列表
     * 
     * @param userId
     * @return
     */
    List<ShoppingCartDTO> list(String userId, String language);

    /**
     * 批量添加购物车商品
     * 
     * @param userId
     */
    void createList(List<ShoppingCartCmd> cmds, String userId, String language);

    /**
     * 新增购物车商品
     *
     * @param cmd
     * @param userId
     */
    BaseId create(ShoppingCartCmd cmd, String userId, String language);

    /**
     * 修改购物车商品
     * 
     * @param cmd
     */
    void update(ShoppingCartUpdateCmd cmd, String userId);

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
