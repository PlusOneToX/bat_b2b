package com.bat.flexible.dao.label.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class LabelDO implements Serializable {
    private static final long serialVersionUID = -7527144142345714564L;
    private Integer id;

    private String name;

    private String templateFile;

    private Short type;

    private Short openFlag;

    private BigDecimal barCodePositionX;

    private BigDecimal barCodePositionY;

    private BigDecimal barCodePositionWidth;

    private BigDecimal barCodePositionHeight;

    private BigDecimal productNamePositionX;

    private BigDecimal productNamePositionY;

    private BigDecimal productNamePositionWidth;

    private BigDecimal productNamePositionHeight;

    private Integer productNamePositionFont;

    private Integer productNamePositionFontSize;

    private BigDecimal modelPositionX;

    private BigDecimal modelPositionY;

    private BigDecimal modelPositionWidth;

    private BigDecimal modelPositionHeight;

    private Integer modelPositionFont;

    private Integer modelPositionFontSize;

    private Short scope;

    private Integer categoryId;

    private BigDecimal enNamePositionX;

    private BigDecimal enNamePositionY;

    private BigDecimal enNamePositionWidth;

    private BigDecimal enNamePositionHeight;

    private Integer enNamePositionFont;

    private Integer enNamePositionFontSize;

    private BigDecimal stuffNamePositionX;

    private BigDecimal stuffNamePositionY;

    private BigDecimal stuffNamePositionWidth;

    private BigDecimal stuffNamePositionHeight;

    private Integer stuffNamePositionFont;

    private Integer stuffNamePositionFontSize;

    private BigDecimal stuffEnNamePositionX;

    private BigDecimal stuffEnNamePositionY;

    private BigDecimal stuffEnNamePositionWidth;

    private BigDecimal stuffEnNamePositionHeight;

    private Integer stuffEnNamePositionFont;

    private Integer stuffEnNamePositionFontSize;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short delFlag;

    private Short relevanceUserUpload;

    private String thirdSkuNo;
}