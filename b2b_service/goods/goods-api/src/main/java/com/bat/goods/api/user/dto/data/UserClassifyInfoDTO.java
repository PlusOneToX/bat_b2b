package com.bat.goods.api.user.dto.data;

import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "商品分类信息")
public class UserClassifyInfoDTO implements Serializable {

    /**
     * 推荐分类信息
     */
    private ClassifyRecommendDTO recommend;

    /**
     * 普通分类信息
     */
    private List<UserClassifyDTO> classify;

}
