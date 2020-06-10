package com.bat.flexible.api.exchange.dto;


import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExchangeCardParamDTO implements Serializable {
    private static final long serialVersionUID = -6015506258446332528L;

    private List<ModelTreeCO> modelList;

    private List<MaterialTreeCO> materialList;


}
