package com.bat.warehouse.web.data;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.data.DataImportServiceI;
import com.bat.warehouse.api.data.RedisServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/warehouse/v1/web/admin/u/data")
@Api(tags = "数据迁移")
public class DataImportController {

    @Autowired
    private DataImportServiceI dataImportServiceI;

    @Autowired
    private RedisServiceI redisServiceI;

    @GetMapping
    @ApiOperation(value = "数据迁移")
    public Response importData(@ApiParam("index") @RequestParam("index")Integer index){
        return dataImportServiceI.importData(index);
    }

    @GetMapping(value ="/query")
    @ApiOperation(value = "根据key查询redis数据")
    public Response tree(@ApiParam(value = "key", required = true) @RequestParam(value = "key")String key){
        Object obj = redisServiceI.getByKey(key);
        return Response.of(obj);
    }

}
