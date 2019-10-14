package com.ncu.oa.admin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable{
	
	private static final long serialVersionUID = -227437593919820521L;
	private Integer id;
	private String roleName;
	private String roleDesc;
	private int parentId;
	private Date createTime;
	private Date updateTime;
}
