# MoonstoneTest

## 介绍

[moonstone](https://gitee.com/azurite_y/moonstone) 内嵌到 Fluorite 中作为 Servlet Web 容器运作的测试项目，但是运行本项目时有以下要求：

1. 当前项目还无法运行于 IDEA 只能运行在 Eclipse。而且必须先导入 Fluorite、Moonstone 项目到 Eclipse 工作空间。其原因参见 [fluorite-test](https://gitee.com/azurite_y/fluorite-test)。

## 使用测试

1. 该项目有预置了静态资源和静态网页，可使用如下 URL 测试

| URL                                             | Method                                      |
| ----------------------------------------------- | ------------------------------------------- |
| http://localhost:8888/images/head.jpg           | [GET]                                       |
| http://localhost:8888/cookies                   | [GET、POST]                                 |
| http://localhost:8888/fileUpload                | [GET、POST]                                 |
| http://localhost:8888/fileDownload              | [GET、POST]                                 |
| http://localhost:8888/forwardToFileDownload     | [GET] -- 测试Forward                        |
| http://localhost:8888/redirectToFileDownload    | [GET] -- 测试Redirect                       |
| http://localhost:8888/include/fileDownload      | [GET] -- 测试RequestDispatcher的include功能 |
| http://localhost:8888/include/requestDispatcher | [GET] -- 测试RequestDispatcher的include功能 |


实现上述URL功能的Moonstone-DefaultServlet方法


```java
@Override
private void initServletMapping() {
    ServletMapping headImg = new ServletMapping("/head.jpg", true);
	servletStaticResourceMapping.put("/head.jpg", headImg);
		
	//--
	ServletMapping cookies = new ServletMapping("/cookies", true);
	servletStaticResourceMapping.put("/cookies", cookies);
	cookies.setPostCallback(new HttpServletServicePostCallback() {
		@Override
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {
			Cookie[] cookiesObj = req.getCookies();
			for (Cookie cookie : cookiesObj) {
				System.out.println("====");
				String cookieName = cookie.getName();
				String value = cookie.getValue();
				String domain = cookie.getDomain();
				int maxAge = cookie.getMaxAge();
				String path = cookie.getPath();
				boolean secure = cookie.getSecure();
				int version = cookie.getVersion();
				String comment = cookie.getComment();
	
				System.out.println("cookieName：" + cookieName);
				System.out.println("value：" + value);
				System.out.println("domain：" + domain);
				System.out.println("maxAge：" + maxAge);
				System.out.println("path：" + path);
				System.out.println("secure：" + secure);
				System.out.println("version：" + version);
				System.out.println("comment：" + comment);
				System.out.println("====");
			}
	
			Cookie cookie = new Cookie("MYSELFCOOKIE", "123456789");
			cookie.setMaxAge(60 * 60 * 24); // 一天后过期
			cookie.setPath("/"); // 在这个路径下面的页面才可以访问该Cookie
			// 如果设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法访问该Cookie
	        cookie.setHttpOnly(false);  
	        
			resp.addCookie(cookie);
	
			resp.getWriter().print("[cookies] Running -  Self Cookie Add");				
		}
	});

    
	//--
	ServletMapping fileUpload = new ServletMapping("/fileUpload", true);
	servletStaticResourceMapping.put("/fileUpload", fileUpload);
	fileUpload.setPostCallback(new HttpServletServicePostCallback() {
		@Override
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {
			Map<String, String[]> parameterMap = req.getParameterMap();
			Enumeration<String> parameterNames = req.getParameterNames();
			while(parameterNames.hasMoreElements()) {
				String parameterName = parameterNames.nextElement();
				logger.info("paramete. {}: {}", parameterName, 
	                        Arrays.asList(parameterMap.get(parameterName) ));
			}
				
			System.out.println("==Part==");
			Collection<Part> parts = req.getParts();
			for (Part part : parts) {
				/**
				 * getName：multipart form 形式返回的参数名
				 * getSubmittedFileName：返回客户端文件系统中的原始文件名
				 */
				System.out.println( String.format("name: [%s], submittedFileName: [%s], size: [%s], contentType：[%s]", part.getName(), part.getSubmittedFileName(), part.getSize(), part.getContentType()) );
				Collection<String> headerNames = part.getHeaderNames();
				headerNames.forEach((headName) -> {
					System.out.println(String.format("\theaderName: [%s], headValue: [%s]", headName, part.getHeaders(headName)));
				});
			}
			System.out.println("==Part==");
			
			resp.getWriter().print("[fileUpload] Running");				
		}
	});
		
	
    //--
	ServletMapping forwardToFileDownload = new ServletMapping("/forwardToFileDownload");
	servletStaticResourceMapping.put("/forwardToFileDownload", forwardToFileDownload);
	forwardToFileDownload.setCallback(new HttpServletServiceCallback() {
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("Forward Request. request: {}, forwardReq: {}", req.getRequestURI(), "/fileDownload");
			}
	
			req.getRequestDispatcher("/fileDownload").forward(req, resp);				
		}
	});

    
	//--
	ServletMapping redirectToFileDownload = new ServletMapping("/redirectToFileDownload");
	servletStaticResourceMapping.put("/redirectToFileDownload", redirectToFileDownload);
	redirectToFileDownload.setCallback(new HttpServletServiceCallback() {
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("Redirect Request. request: {}, redirectReq: {}", req.getRequestURI(), "/fileDownload");
			}
			resp.sendRedirect("/fileDownload");				
		}
	});


	//--
	ServletMapping fileDownloadInclude = new ServletMapping("/include/fileDownload");
	servletStaticResourceMapping.put("/include/fileDownload", fileDownloadInclude);
	fileDownloadInclude.setCallback(new HttpServletServiceCallback() {
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, 
	    IOException {
			req.setAttribute("includeKey", "fileDownloadInclude-value");
				
			if (logger.isDebugEnabled()) {
				logger.debug("URI: [/redirectToFileDownload], Set Attribute: [includeKey]");
			}
			resp.getWriter().print("[fileDownloadInclude] Running");				
		}
	});


	//--
	ServletMapping requestIncludeDispatcher = new ServletMapping("/include/requestDispatcher");
	servletStaticResourceMapping.put("/include/requestDispatcher", requestIncludeDispatcher);
	requestIncludeDispatcher.setCallback(new HttpServletServiceCallback() {
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, 
	    IOException {
			req.getRequestDispatcher("/include/fileDownload").include(req, resp);
				
			Object attribute = req.getAttribute("includeKey");
			if (logger.isDebugEnabled()) {
				logger.debug("URI: [{}], Get Attribute. name: [{}], value: [{}]", req.getRequestURI(), "includeKey", attribute);
			}
			resp.getWriter().print("[RequestDispatcherIncludeTest] Running");				
		}
	});
}
```
