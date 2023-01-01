package com.zy.test;

import java.util.Map;

import org.zy.fluorite.core.utils.PropertiesUtils;

/**
 * @dateTime 2022年12月9日;
 * @author zy(azurite-Y);
 * @description
 */
public class JSONTest {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Map<Object, Object> properties = PropertiesUtils.load("application.properties", "UTF-8", false);
		Object object = properties.get("user.map-of-list");
		System.out.println(object);
		String objectStr = (String)object;
		
//		MapOfList parseObject = JSON.parseObject(objectStr, MapOfList.class);
//		Map<String, List<String>> parseObject2 = JSON.parseObject(objectStr, Map.class);
//		System.out.println(parseObject);
//		System.out.println(parseObject2);
	}
}
