/**
 * 
 */
package algz.platform.core.configure.WebAppInitializer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author algz
 * 应用容器加载后第一个调用
 */
public class AppWebAppInitializer implements WebApplicationInitializer {

	
	public void onStartup(ServletContext servletContext){
		  /**Spring配置文件开始 */
	      // Create the 'root' Spring application context
	      AnnotationConfigWebApplicationContext rootContext =new AnnotationConfigWebApplicationContext();
	      rootContext.register(AppConfig.class);
	      
	      // Register ServletRequestListener
	      servletContext.addListener(new ContextLoaderListener(rootContext));
	      /**Spring配置文件结束*/
	      
	    /**SpringMVC配置文件开始*/
		//XmlWebApplicationContext appContext = new XmlWebApplicationContext();
		//appContext.setConfigLocation("classpath:*algz/platform/core/configure/xml/spring-mvc-servlet.xml");
		// Register Servlet // Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		/**SpringMVC配置文件结束*/
		
        // Register Filter 
//        FilterRegistration filter = servletContext.addFilter("springSecurityFilterChain", 
//            DelegatingFilterProxy.class); 
//        filter.addMappingForUrlPatterns(null, true, "/*"); 
		
	  /**设置servlet编码开始*/
      // Register Filter : Set Character Encoding
      FilterRegistration scefilter = servletContext.addFilter("Set Character Encoding", CharacterEncodingFilter.class); 
      Map<String,String> sceMap=new HashMap<String,String>();
      sceMap.put("encoding", "UTF-8");
      sceMap.put("forceEncoding", "true");
      scefilter.setInitParameters(sceMap);
      scefilter.addMappingForUrlPatterns(null, true, "/*"); 
      /**设置servlet编码结束*/
	}

}
