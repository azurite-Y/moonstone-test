package com.zy.test;

import org.junit.jupiter.api.Test;
import org.zy.fluorite.autoconfigure.web.server.moonstone.MoonStoneEmbeddedWebappClassLoader;
import org.zy.fluorite.core.utils.ClassUtils;
import org.zy.moonStone.core.container.context.StandardContext;
import org.zy.moonStone.core.exceptions.LifecycleException;
import org.zy.moonStone.core.loaer.WebappLoader;
import org.zy.moonStone.core.webResources.StandardRoot;

/**
 * @dateTime 2022年12月20日;
 * @author zy(azurite-Y);
 * @description
 * {@link StandardContext}
 * BootStrap
 */
@SuppressWarnings("unused")
public class WebClassLoaderTest {
	private String classPath = "D:\\Workspace\\Eclipse\\MoonStoneApp\\target\\classes";
	
	private StandardContext standardContext = new StandardContext();
	private StandardRoot standardRoot = new StandardRoot(standardContext);
	private WebappLoader loader = new WebappLoader();

	
	{
//		standardContext.setDocBase(classPath);
//		standardContext.setParentClassLoader(ClassUtils.getDefaultClassLoader());
//		standardContext.setResources(standardRoot);
//		standardContext.setReloadable(true);
//		
//		loader.setLoaderClass(MoonStoneEmbeddedWebappClassLoader.class.getName());
//		loader.setDelegate(false);
//		standardContext.setLoader(loader);

//		try {
//			standardRoot.init();
//			standardRoot.start();
//			
//			loader.start();
//			
//			Thread.currentThread().setContextClassLoader(loader.getClassLoader());
//		} catch (LifecycleException e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void threadContextClassLoader() throws Exception {
		Class<?> userClz = Thread.currentThread().getContextClassLoader().loadClass("com.zy.config.User");
		System.out.println(userClz);
		System.out.println("==========");
//		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//		System.out.printf("contextClassLoader: %s\n", contextClassLoader); // sun.misc.Launcher$AppClassLoader
		
		/**
		 * @param name - 所需类的完全限定名称
		 * @param initialize - 如果为true，该类将被初始化。
		 * @param loader - 必须从中加载类的类加载器
		 */
		Class<?> forName = Class.forName("com.zy.config.Admin", false, Thread.currentThread().getContextClassLoader());
		System.out.println(forName);
	}
}
