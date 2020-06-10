package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import lombok.Data;
import java.util.List;

@Data
public class ExchangeCodeUserDetailDTO {

    private List<MaterialRelaSimpleDTO> materialRelas;

    private ExchangeCodeUserCO exchangeCodeUser;
}
