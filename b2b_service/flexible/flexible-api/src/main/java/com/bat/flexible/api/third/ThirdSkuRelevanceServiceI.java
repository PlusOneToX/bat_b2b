package com.bat.flexible.api.third;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.third.dto.page.ThirdSkuRelevancePageQry;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.third.co.ThirdSkuRelevancePageCO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.Map;

public interface ThirdSkuRelevanceServiceI {

    PageInfo<ThirdSkuRelevancePageCO> page(ThirdSkuRelevancePageQry thirdSkuRelevancePageQry);

    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    Response deteleBatch(FlexibleIdListDTO flexibleIdListDTO, AdminResponse currentAdmin);

    HSSFWorkbook export(ThirdSkuRelevancePageQry thirdSkuRelevancePageQry);

    Map<String, Object> deleteByDistributorId(Integer distributorId, AdminResponse currentAdmin);

    /**
     * 导入sku
     * @param inputStream
     * @param currentAdmin
     * @param distributorId
     * @return
     */
    Response importSku(InputStream inputStream, AdminResponse currentAdmin, Integer distributorId);

    void insertAgain(Map<String, Object> map);

    HSSFWorkbook downLoadExcel();

    /**
     * 根据分销商id和第三方sku编码查询
     * @param distributorId
     * @param sku
     * @return
     */
    ThirdSkuRelevanceDO getByDistributorIdAndThirdSkuNo(Integer distributorId, String sku);
}
