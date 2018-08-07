package com.bat.order.service.flexible.validator;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.picture.api.PictureServiceRpc;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.service.common.constant.FlexibleRpcConstant;
import com.bat.order.service.common.error.FlexibleRpcErrorCode;
import com.bat.order.service.common.error.OrderNameErrorCode;

@Component
public class PictureValidator {

    private static PictureServiceRpc pictureServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 5000)
    public void setPictureServiceRpc(PictureServiceRpc pictureServiceRpc) {
        PictureValidator.pictureServiceRpc = pictureServiceRpc;
    }

    public static void validPictureUsable(PictureDTORpcQry pictureDTORpcQry) {
        if (pictureDTORpcQry == null) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_PICTURE)
                + MessageUtils.get(FlexibleRpcErrorCode.FLEXIBLE_ID_ERROR));
        }
        validOpenFlag(pictureDTORpcQry.getOpenFlag());
    }

    public static void validOpenFlag(Short openFlag) {
        if (FlexibleRpcConstant.COMMON_OPEN_FLAG_NO.equals(openFlag)) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_PICTURE)
                + MessageUtils.get(FlexibleRpcErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE));
        }
    }

    public static PictureDTORpcQry getUsableByPictureId(Integer pictureId) {

        Response<PictureDTORpcQry> response = pictureServiceRpc.getDTORpcById(pictureId);
        PictureDTORpcQry pictureDTORpcQry = response.getData();
        validOpenFlag(pictureDTORpcQry.getOpenFlag());
        return pictureDTORpcQry;
    }

    /**
     * 根据图片id获取图片对象、不做校验
     * 
     * @param pictureId
     * @return
     */
    public static PictureDTORpcQry getByPictureIdWithoutValid(Integer pictureId) {

        Response<PictureDTORpcQry> response = pictureServiceRpc.getDTORpcById(pictureId);
        return response.getData();
    }
}
