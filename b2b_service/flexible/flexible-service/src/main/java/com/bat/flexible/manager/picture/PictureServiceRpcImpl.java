package com.bat.flexible.manager.picture;

import com.bat.flexible.manager.picture.convertor.PictureConvertor;
import com.bat.flexible.manager.picture.executor.PictureQryExe;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.picture.api.PictureServiceRpc;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class PictureServiceRpcImpl implements PictureServiceRpc {

    @Autowired
    private PictureQryExe pictureQryExe;


    /**
     * 根据图片id获取对象
     * @param id 图片id
     * @return
     */
    @Override
    public Response<PictureDTORpcQry> getDTORpcById(Integer id) {
        PictureDO pictureDO = pictureQryExe.getById(id);
        return Response.of(PictureConvertor.toRpcDTO(pictureDO));
    }
}
