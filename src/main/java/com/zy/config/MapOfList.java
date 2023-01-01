package com.zy.config;

import java.util.List;

/**
 * @dateTime 2022年12月9日;
 * @author zy(azurite-Y);
 * @description
 */
public class MapOfList {
	private String str;
	private List<String> list;
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	
	public MapOfList(String str, List<String> list) {
		super();
		this.str = str;
		this.list = list;
	}
	public MapOfList() {
		super();
	}
}
