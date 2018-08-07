package com.bat.system.service.check.convertor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.base.ErrorCode;
import com.bat.system.dao.check.dataobject.CheckConfigDO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.common.Response;

import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/05/26 14:18
 */
public class CheckConfigRpcConvertor {
    @SneakyThrows
    private static Object getObject(CheckConfigDO one, Class target) {
        Object o = target.newInstance();
        BeanUtils.copyProperties(one, o);
        return o;
    }

    public static Response toResponse(CheckConfigDO one, Class target) {
        if (one != null) {
            Object o = getObject(one, target);
            return Response.of(o);
        }
        return Response.buildFailure(ErrorCode.B_RPC_NULL, "rpc 返回内容为空");
    }

    public static Response toResponseList(List<CheckConfigDO> listCheckByParams, Class target) {
        if (!CollectionUtils.isEmpty(listCheckByParams)) {
            List<Object> collect = listCheckByParams.stream().map(CheckConfigDO -> getObject(CheckConfigDO, target))
                .collect(Collectors.toList());
            return Response.of(collect);
        }
        return Response.buildFailure(ErrorCode.B_RPC_NULL, "rpc 返回内容为空");
    }
}
