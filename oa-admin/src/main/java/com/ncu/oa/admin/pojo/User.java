package com.ncu.oa.admin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable{

	private static final long serialVersionUID = -5440372534300871944L;
	
	private Integer id;
	private String staffId;
	private String password;
	private String passwordSalt;
	private int status;
	private Date createTime;
	private Date updateTime;
}
