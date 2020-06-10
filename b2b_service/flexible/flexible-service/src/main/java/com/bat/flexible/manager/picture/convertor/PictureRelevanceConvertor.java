package com.bat.flexible.manager.picture.convertor;

import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.dao.picture.dataobject.PictureLabelRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PictureRelevanceConvertor {


    private static PictureServiceI pictureServiceI;


    private static PictureCategoryServiceI pictureCategoryServiceI;

    public static List<PictureRelaSimpleDTO> toPictureRelaSimpleDTOListFromMaterial(List<PictureMaterialRelevanceDO> pictureMaterialList) {
        if(pictureMaterialList ==null || pictureMaterialList.size()==0){
            return null;
        }
        List<PictureRelaSimpleDTO> list = new ArrayList<>();
        pictureMaterialList.stream().forEach(pictureMaterialRelevanceDO -> {
            PictureRelaSimpleDTO pictureRelaSimpleDTO = new PictureRelaSimpleDTO();
            pictureRelaSimpleDTO.setPictureId(pictureMaterialRelevanceDO.getPictureId());
            pictureRelaSimpleDTO.setId(pictureMaterialRelevanceDO.getId());
            list.add(pictureRelaSimpleDTO);
        });
        setPictureAndCategoryName(list);
        return list;
    }

    public static List<PictureRelaSimpleDTO> toPictureRelaSimpleDTOListFromLabel(List<PictureLabelRelevanceDO> pictureLabelRelevanceDOList) {
        if(pictureLabelRelevanceDOList ==null || pictureLabelRelevanceDOList.size()==0){
            return null;
        }
        List<PictureRelaSimpleDTO> list = new ArrayList<>();
        pictureLabelRelevanceDOList.stream().forEach(pictureLabelRelevanceDO -> {
            PictureRelaSimpleDTO pictureRelaSimpleDTO = new PictureRelaSimpleDTO();
            pictureRelaSimpleDTO.setPictureId(pictureLabelRelevanceDO.getPictureId());
            pictureRelaSimpleDTO.setId(pictureLabelRelevanceDO.getId());
            list.add(pictureRelaSimpleDTO);
        });
        setPictureAndCategoryName(list);
        return list;
    }

    @Resource
    public  void setPictureServiceI(PictureServiceI pictureServiceI) {
        PictureRelevanceConvertor.pictureServiceI = pictureServiceI;
    }

    @Resource
    public  void setPictureCategoryServiceI(PictureCategoryServiceI pictureCategoryServiceI) {
        PictureRelevanceConvertor.pictureCategoryServiceI = pictureCategoryServiceI;
    }

    /**
     * 给图片关联赋值
     * @param list
     * @return
     */
    public static void setPictureAndCategoryName(List<PictureRelaSimpleDTO> list){


        if(list ==null || list.size()==0){
            return ;
        }

        //设置图片
        List<PictureDO> pictureDOList = pictureServiceI.listByCondition(null, null);
        //图片map
        Map<Integer, PictureDO> pictureDOMap = pictureDOList.stream().collect(Collectors.toMap(PictureDO::getId, pictureDO -> pictureDO));
        //图片分类
        List<PictureCategoryDO> pictureCategoryDOList = pictureCategoryServiceI.listByCondition(null);
        Map<Integer, PictureCategoryDO> pictureCategoryDOMap = pictureCategoryDOList.stream().collect(Collectors.toMap(PictureCategoryDO::getId,pictureCategoryDO -> pictureCategoryDO));
        list.stream().forEach(pictureRelaSimpleDTO -> {
            PictureDO pictureDO = pictureDOMap.get(pictureRelaSimpleDTO.getPictureId());
            if(pictureDO!=null) {
                pictureRelaSimpleDTO.setName(pictureDO.getName());
                pictureRelaSimpleDTO.setEnglishName(pictureDO.getEnglishName());
                PictureCategoryDO pictureCategoryDO = pictureCategoryDOMap.get(pictureDO.getCategoryId());
                pictureRelaSimpleDTO.setCategoryName(pictureCategoryDO.getName());
                pictureRelaSimpleDTO.setCategoryEnglishName(pictureCategoryDO.getEnglishName());
            }
        });
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        List<PictureMaterialRelevanceDO> list = new ArrayList<>();
        PictureMaterialRelevanceDO relevanceDO = new PictureMaterialRelevanceDO();
        relevanceDO.setId(1);
        relevanceDO.setPictureId(12);
        list.add(relevanceDO);
        System.out.println(list.getClass().getTypeName());
       /* Type genericSuperclass = list.getClass().getGenericSuperclass();
        System.out.println(JSON.toJSONString(genericSuperclass));
        ParameterizedType pt = (ParameterizedType) genericSuperclass;

        Class<?> cls =(Class<?>) pt.getActualTypeArguments()[0];
        Field idField = cls.getDeclaredField("id");
        System.out.println(idField.get(cls));*/

       /* for(Field field : declaredFields){
            field.setAccessible(true);


                Type type = field.getGenericType();
                if(type ==null){
                    continue;
                }
                if(type instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    //得到泛型的对象
                    Class aClass =(Class) parameterizedType.getActualTypeArguments()[0];
                    System.out.println(JSON.toJSONString(aClass));
                }


        }*/
    }
}
