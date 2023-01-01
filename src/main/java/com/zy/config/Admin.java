package com.zy.config;

/**
 * @dateTime 2022年12月9日;
 * @author zy(azurite-Y);
 * @description
 */
public class Admin {
	private String name;
	private Integer pwd;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPwd() {
		return pwd;
	}
	public void setPwd(Integer pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return "Admin [name=" + name + ", pwd=" + pwd + "]";
	}
}
