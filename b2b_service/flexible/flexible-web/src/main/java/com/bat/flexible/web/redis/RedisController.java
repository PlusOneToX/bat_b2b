package com.bat.flexible.web.redis;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.redis.RedisServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/u/redis")
@Api(tags = "查询redis数据接口、上线删除")
public class RedisController {

    @Autowired
    private RedisServiceI redisServiceI;


    @GetMapping(value ="/query")
    @ApiOperation(value = "根据key查询redis数据")
    public Response tree(@ApiParam(value = "key", required = true) @RequestParam(value = "key")String key){
        Object obj = redisServiceI.getByKey(key);
        return Response.of(obj);
    }




}
