package com.bat.flexible.manager.picture.convertor;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.picture.co.PicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PictureConvertor {

    public static void setMaterialNameByIds(List<PicturePageCO> list, MaterialServiceI materialServiceI){
        if(list ==null || list.size()==0 || materialServiceI==null){
            return;
        }
        Map<Integer, MaterialDO> map = new HashMap<>();
        list.stream().forEach(picturePageCO -> {
           if(StringUtils.isNotBlank(picturePageCO.getMaterialIdS())){
               String[] materialIdArr = picturePageCO.getMaterialIdS().split(",");
               String materialName=null;
               for(int x=0;x<materialIdArr.length;x++){
                   MaterialDO materialDO = map.get(materialIdArr[x]);
                   if(materialDO==null){
                       materialDO = materialServiceI.getById(Integer.parseInt(materialIdArr[x]));
                       map.put(materialDO.getId(),materialDO);
                   }
                   if(materialName ==null){
                       materialName=materialDO.getName();
                   }else{
                       materialName=materialName+","+materialDO.getName();
                   }
               }
               picturePageCO.setMaterialNameS(materialName);
           }
        });
    }

    public static PictureDTORpcQry toRpcDTO(PictureDO pictureDO){

        return BeanUtils.copy(pictureDO,PictureDTORpcQry.class);
    }


}
