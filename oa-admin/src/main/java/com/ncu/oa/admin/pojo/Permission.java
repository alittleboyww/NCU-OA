package com.ncu.oa.admin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class Permission implements Serializable{
	private static final long serialVersionUID = 7160557680614732403L;
	private Integer id;
	private String permissionName;
	private String permissionDesc;
	private Date createTime;
	private Date updateTime;
}
