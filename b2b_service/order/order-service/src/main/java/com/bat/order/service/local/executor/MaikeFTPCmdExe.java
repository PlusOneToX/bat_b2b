package com.bat.order.service.local.executor;

import java.util.List;

import com.bat.order.service.common.error.DubboServiceErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.maike.api.ThirdPartyMaikeServiceRpc;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailFTPDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;
import com.bat.order.api.common.exception.OrderException;

@Component
public class MaikeFTPCmdExe {

    @DubboReference(check = false, timeout = 600000, retries = 0)
    private ThirdPartyMaikeServiceRpc thirdPartyMaikeServiceRpc;

    /**
     * 上传到FTP
     * 
     * @param uploadCmdList
     * @return
     */
    public List<OrderDetailFTPDTO> uploadFTP(List<OrderDetailLocalUploadCmd> uploadCmdList) {
        Response<List<OrderDetailFTPDTO>> response = thirdPartyMaikeServiceRpc.updateToFTP(uploadCmdList);
        if (response == null) {
            throw OrderException.buildException(DubboServiceErrorCode.DUBBO_THIRD_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }
}
