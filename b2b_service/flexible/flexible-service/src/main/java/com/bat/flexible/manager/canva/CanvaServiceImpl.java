package com.bat.flexible.manager.canva;

import com.bat.flexible.api.canva.CanvaServiceI;
import com.bat.flexible.manager.common.utils.CommonUtil;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.api.FlexibleCustomException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CanvaServiceImpl implements CanvaServiceI {


    @Value("${canva.apiKeyId}")
    private String canvaApiKeyId;
    @Value("${canva.apiSecretKey}")
    private String canvaApiSecretKey;

    @Override
    public String getToken(String userNo) {
        try {

            return CommonUtil.encodeJWT(canvaApiKeyId,canvaApiSecretKey,userNo);
        } catch (JoseException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(FlexibleCommonErrorCode.C_CANVAS_TOKEN_QUERY_FAIL);
        }
    }
}
