package com.bat.dubboapi.order.order.dto.info;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderIdListQry implements Serializable {


    private static final long serialVersionUID = -1867887399046654378L;


    private List<Integer> idList;
}
