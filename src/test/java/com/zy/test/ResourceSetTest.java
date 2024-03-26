package com.zy.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;

import org.junit.jupiter.api.Test;
import org.zy.moonstone.core.container.context.StandardContext;
import org.zy.moonstone.core.exceptions.LifecycleException;
import org.zy.moonstone.core.interfaces.webResources.WebResource;
import org.zy.moonstone.core.util.http.FastHttpDateFormat;
import org.zy.moonstone.core.webResources.DirResourceSet;
import org.zy.moonstone.core.webResources.FileResource;
import org.zy.moonstone.core.webResources.StandardRoot;
import org.zy.moonstone.core.webResources.WarResourceSet;
/**
 * @dateTime 2022年12月16日;
 * @author zy(azurite-Y);
 * @description 测试 {@link DirResourceSet } 和 {@link WarResourceSet } {@link StandardRoot}
 */
public class ResourceSetTest {
	private String testClassPath = ClassLoader.getSystemResource("").getFile();
	private String classPath = "D:\\Workspace\\Eclipse\\MoonStoneApp\\target\\classes";



	@Test
	public void resourceSetTest() throws LifecycleException {
		StandardContext standardContext = new StandardContext();
		StandardRoot standardRoot = new StandardRoot(standardContext);
		standardContext.setDocBase(testClassPath);
		System.out.println(testClassPath);
		standardContext.setResources(standardRoot);

		try {
			standardRoot.init();
			standardRoot.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}

		String[] list3 = standardRoot.list("/templates");
		System.out.println(Arrays.asList(list3));

		// 因为 StandardRoot 已启动，所以当前实例化的任何 WebResourceSet 对象都会在构造实例时启动
		DirResourceSet dirResourceSet = new DirResourceSet(standardRoot, "/", testClassPath, "/");
		String[] list = dirResourceSet.list("/");
		System.out.printf( "resource list: %s\n", Arrays.asList(list) );

		//		WebResource headImage = dirResourceSet.getResource("/static/images/head.jpg");
		//		WebResource resource3 = dirResourceSet.getResource("/templates/404.html");

		WebResource resource4 = standardRoot.getResource("/static/images/head.jpg");
		consoleDisplay("DirResourceSet - /static/images/head.jpg", resource4);

		WebResource resource5 = standardRoot.getResource("/logs");
		consoleDisplay("DirResourceSet - /logs", resource5);


		WebResource resource = dirResourceSet.getResource("/dubbo-admin-2.6.0.war");
		WarResourceSet warResourceSet = new WarResourceSet(standardRoot, "/", resource.getCanonicalPath());

		WebResource resource2 = warResourceSet.getResource("/WEB-INF/classes");
		consoleDisplay("WarResourceSet - WebResource", resource2);

		String[] list2 = warResourceSet.list("/");
		System.out.printf( "resource2 list: %s\n", Arrays.asList(list2) );
	}

	private void consoleDisplay(String displayName, WebResource resource) {
		System.out.println("=====" + displayName + "=====");
		System.out.printf("WebResourceImplClaz: %s\n", resource.getClass());

		if (resource.exists()) {
			System.out.printf("ContentLength: %s\n", resource.getContentLength());
			System.out.printf("Name: %s\n", resource.getName());
			System.out.printf("MimeType: %s\n", resource.getMimeType());
			System.out.printf("Creation: %s\n", FastHttpDateFormat.formatDayTime( resource.getCreation()) );
			System.out.printf("LastModified: %s\n", FastHttpDateFormat.formatDayTime( resource.getLastModified()) );
			System.out.printf("isDirectory: %s\n", resource.isDirectory());
			System.out.printf("isFile: %s\n", resource.isFile());
			System.out.printf("UR: %s\n", resource.getURL());
		}
		System.out.println("==========\n");
	}

	@Test
	public void listFile() {
		StandardContext standardContext = new StandardContext();
		StandardRoot standardRoot = new StandardRoot(standardContext);
		standardContext.setDocBase(classPath);
		standardContext.setResources(standardRoot);

		try {
			standardRoot.init();
			standardRoot.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}

		List<FileResource> WebResources = new ArrayList<>();

		WebResource resource = standardRoot.getResource("/");
		Manifest manifest = resource.getManifest();
		File rootFIle = new File(resource.getCanonicalPath());
		String rootPath = resource.getCanonicalPath();

		for (File fileResource : rootFIle.listFiles()) {
			if (fileResource.getName().equals("static") || fileResource.getName().equals("templates")) {
				continue;
			}

			if (fileResource.isFile() && fileResource.getName().endsWith(".class") && fileResource.canRead()) {
				String substrPath = fileResource.getPath().substring(rootPath.length());
				substrPath = substrPath.replace("\\", "/");
				WebResources.add(new FileResource(standardRoot, substrPath, fileResource, !fileResource.canWrite(), manifest));
			} else if (fileResource.isDirectory()) {
				recursionListFile(fileResource, WebResources, standardRoot, rootPath, manifest);
			}
		}

		for (FileResource webResource : WebResources) {
			System.out.println(webResource.getCanonicalPath());
		}

	}

	private void recursionListFile(File WebResource, List<FileResource> WebResources, StandardRoot standardRoot, String rootPath, Manifest manifest) {
		for (File fileResource : WebResource.listFiles()) {
			if (fileResource.isFile() && fileResource.getName().endsWith(".class") && fileResource.canRead()) {
				String substrPath = fileResource.getPath().substring(rootPath.length());
				substrPath = substrPath.replace("\\", "/");
				WebResources.add(new FileResource(standardRoot, substrPath, fileResource, !fileResource.canWrite(), manifest));
			} else if (fileResource.isDirectory()) {
				recursionListFile(fileResource, WebResources, standardRoot, rootPath, manifest);
			}
		}
	}
}
