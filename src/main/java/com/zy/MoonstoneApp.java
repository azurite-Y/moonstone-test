package com.zy;

import java.io.IOException;

import org.zy.fluorite.autoconfigure.web.ServerProperties;
import org.zy.fluorite.boot.FluoriteApplication;
import org.zy.fluorite.boot.annotation.RunnerAs;
import org.zy.fluorite.context.interfaces.ConfigurableApplicationContext;
import org.zy.fluorite.core.annotation.Autowired;
import org.zy.moonstone.core.servlets.DefaultServlet;

/**
 * @dateTime 2022年12月8日;
 * @author zy(azurite-Y);
 * @description
 * {@link DefaultServlet}
 * NioSocketWrapper
 */
@RunnerAs
public class MoonstoneApp {
	
	@Autowired
	private ServerProperties serverProperties;
	
	public static void main(String[] args) throws IOException {
		System.setProperty("java.io.tmpdir", ClassLoader.getSystemClassLoader().getResource("").getFile());
		ConfigurableApplicationContext run = FluoriteApplication.run(MoonstoneApp.class, args);
		
		MoonstoneApp bean = run.getBean(MoonstoneApp.class);
		System.out.println(bean.serverProperties.getPort());
	}
}
