package com.bat.flexible.web.data;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.data.SqlDataServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flexible/v1/web/admin/u/sql/data")
@Api(tags = "数据迁移接口")
public class SqlDataController {

    @Autowired
    private SqlDataServiceI sqlDataServiceI;


    @GetMapping(value = "/import")
    @ApiOperation(value = "迁移sql")
    public Response importSql(@ApiParam(value = "index") @RequestParam(value = "index")Integer index){
        return sqlDataServiceI.importSql(index);
    }
}
