package com.zy.config;

import java.util.List;
import java.util.Map;

import org.zy.fluorite.context.annotation.conditional.ConditionalOnMissingBean;
import org.zy.fluorite.core.annotation.Component;
import org.zy.fluorite.core.annotation.ConfigurationProperties;
import org.zy.fluorite.core.annotation.NestedConfigurationProperty;

/**
 * @dateTime 2022年12月8日;
 * @author zy(azurite-Y);
 * @description
 */
@Component
@ConfigurationProperties(prefix = "user", ignoreInvalidFields = false)
public class User {
	private String name;
	private String pwd;
	private int age;
	private List<Integer> list;
	private Map<String,String> map;
	private Map<String, List<String>> mapOfList;
	
	@NestedConfigurationProperty
	private Admin admin;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public Map<String, List<String>> getMapOfList() {
		return mapOfList;
	}
	public void setMapOfList(Map<String, List<String>> mapOfList) {
		this.mapOfList = mapOfList;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", pwd=" + pwd + ", age=" + age + ", list=" + list + ", map=" + map
				+ ", mapOfList=" + mapOfList + ", admin=" + admin + "]";
	}
	
	
}
