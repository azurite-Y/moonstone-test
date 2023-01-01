package com.zy;

import java.io.IOException;

import org.zy.fluorite.autoconfigure.web.ServerProperties;
import org.zy.fluorite.boot.FluoriteApplication;
import org.zy.fluorite.boot.annotation.RunnerAs;
import org.zy.fluorite.context.interfaces.ConfigurableApplicationContext;
import org.zy.fluorite.core.annotation.Autowired;
import org.zy.moonStone.core.servlets.DefaultServlet;

import com.zy.config.User;

/**
 * @dateTime 2022年12月8日;
 * @author zy(azurite-Y);
 * @description
 * {@link DefaultServlet}
 */
@RunnerAs
public class MoonStoneApp {
	
	@Autowired
	private User user;
	
	@Autowired
	private ServerProperties serverProperties;
	
	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext run = FluoriteApplication.run(MoonStoneApp.class, args);
		
		// comment
		MoonStoneApp bean = run.getBean(MoonStoneApp.class);
		// 验证 @ConfigurationProperties
		System.out.println(bean.user);
		System.out.println(bean.serverProperties.getPort());
	}
}
