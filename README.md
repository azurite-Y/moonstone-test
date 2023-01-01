# MoonstoneTest

#### 介绍
[Moonstone](https://gitee.com/azurite_y/MoonStone) 内嵌到Fluorite中作为Servlet Web 容器运作的测试项目


#### 运行教程

1.  克隆运行 MoonStoneApp
```java
@RunnerAs
public class MoonStoneApp {
	@Autowired
	private User user;
	
	@Autowired
	private ServerProperties serverProperties;
	
	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext run = FluoriteApplication.run(MoonStoneApp.class, args);
		MoonStoneApp bean = run.getBean(MoonStoneApp.class);
		// 验证 @ConfigurationProperties
		System.out.println(bean.user);
		System.out.println(bean.serverProperties.getPort());
	}
}
```
2. 该项目有预置了静态资源和静态网页，可使用如下URL测试
```java
2.1. http://localhost:8888/images/head.jpg            	[GET]
2.2. http://localhost:8888/cookies                    	[GET、POST]
2.3. http://localhost:8888/fileUpload 					[GET、POST]
2.4. http://localhost:8888/fileDownload 				[GET、POST]
2.5. http://localhost:8888/forwardToFileDownload 		[GET] -- 测试Forward
2.6. http://localhost:8888/redirectToFileDownload 		[GET] -- 测试Redirect
2.7. http://localhost:8888/include/fileDownload 		[GET] -- 测试RequestDispatcher的include功能
2.8. http://localhost:8888/include/requestDispatcher 	[GET] -- 测试RequestDispatcher的include功能
```
实现上述URL功能的MoonStone-DefaultServlet方法
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


#### 使用说明

1.  当前Fluorite还未开发出如SpringMVC那样的Servlet封装支持，所以当前只能使用MoonStone自带的 DefaultServlet 响应请求。所以静态资源和请求的响应都由其完成。但resources目录下的static和templates目录下的资源，如果有的话MoonStone会自动挂载到根目录下，以符合Servlet映射要求。
2.  当前对内嵌MoonStone的配置粒度有限，参见Fluorite-autoconfigure包下org.zy.fluorite.autoconfigure.web.ServerProperties类
3.  对于响应请求支持基本的Context-Length报文格式和Chunked格式。支持GZIP压缩处理
