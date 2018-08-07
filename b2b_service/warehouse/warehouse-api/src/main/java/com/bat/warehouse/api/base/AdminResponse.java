package com.bat.warehouse.api.base;

import lombok.Data;

@Data
public class AdminResponse {
	
	private Integer id;
	private String userName;
	private String name;
	private String password;
	private String number;
	private String remark;
	private Integer adminType;
	private Integer brandScope;
	private Integer saleScope;
	private Boolean status;
	private Integer organizationId;
	private Integer departmentId;
	private Integer businessUnitId;

	private String roles;

	private String roleNames;

	private String brands;

	private String sales;
	private Long lastOnlineTime;
	private Long times;
	private Long createTime;
	private Long updateTime;
	private String department;
	
	private String mobile;
	private Long rockAccountId=0L;
	private String dingAvatar;
	private String yunId;
	private Short isFictitious;
	private Short isSale;
	

	
	
	

}
