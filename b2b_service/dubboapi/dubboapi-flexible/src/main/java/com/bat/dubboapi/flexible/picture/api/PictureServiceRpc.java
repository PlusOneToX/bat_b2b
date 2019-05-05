package com.bat.dubboapi.flexible.picture.api;

import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.flexible.common.Response;

public interface PictureServiceRpc {

    /**
     * 根据图片id获取图片RPC的对象
     * @param id 图片id
     * @return
     */
    Response<PictureDTORpcQry> getDTORpcById(Integer id);
}
