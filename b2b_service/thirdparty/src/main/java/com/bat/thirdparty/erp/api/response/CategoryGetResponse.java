package com.bat.thirdparty.erp.api.response;


import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.thirdparty.erp.api.response.dto.ProductlineErp;

import java.util.List;

public class CategoryGetResponse extends Response {

    private List<ProductlineErp> productlines;

    public List<ProductlineErp> getProductlines() {
        return productlines;
    }

    public void setProductlines(List<ProductlineErp> productlines) {
        this.productlines = productlines;
    }
}
