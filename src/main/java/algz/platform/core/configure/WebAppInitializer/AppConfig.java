package algz.platform.core.configure.WebAppInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * @EnableWebMvc导入spring_mvc需要的诸多bean，再配合@ComponentScan扫描包里面所有@Component(@Repository @Service  @Constroller)，基本的mvc配置就完成了。
 * @EnableTransactionManagement  //启动事务管理配置
*  @ImportResource导入基于XML方式的配置文件，多个配置文件采{"",""}数组。
* 
*   若要自定义默认配置，在 Java 中您只需实现 WebMvcConfigurer 接口或扩展 WebMvcConfigurerAdapter 类和重写您所需要的方法。下面是一些可用方法重写的例子。
* 
* @author algz
* Spring Config配置(无XML)
*/
@Configuration
//@ImportResource({"classpath:algz/platform/config/xml/SpringSecurity-context.xml"})
@ComponentScan(basePackages = {"algz.platform","com"})//扫描注解组件的包的基础位置(@Controller,@Service...)
@EnableWebMvc //启用 MVC Java config ，在你的 @Configuration类上增加@EnableWebMvc注解
@EnableTransactionManagement  //声明式事务管理，通过spring root application context扫描包septem.config.app：
//@PropertySource("/conf/jdbc.properties")
@Order(1)
public class AppConfig  extends WebMvcConfigurerAdapter{

	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		return  new HibernateTransactionManager(sessionFactory().getObject());
	}


//  //*********************springmvc开始配置*************************
//  /**
//   * Resolve logical view names to .jsp resource in the /WEB-INF/views directory
//   * 解析所有视图为前缀(/WEB-INF/)+视图名称+后缀(.jsp)的文件.
//   springmvc XML:
//       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
//        <property name="prefix">WEB-INF/views/</property>
//        <property name="suffix">.jsp</property>
//      </bean>
//   */
  @Bean
  public ViewResolver viewResolver(){
      InternalResourceViewResolver resolver =new InternalResourceViewResolver();
      resolver.setPrefix("WEB-INF/");
      resolver.setSuffix(".jsp");
      return resolver;
  }
  
  /**
   * 
   * <mvc:resources mapping="/resources/**" location="/resources/" /> 
   * 
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }
  
  @Override  
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {  
      configurer.enable();  
  }  

  /**
   * 三种方式都不到mediaType,那只能是默认的json::http://localhost:8080/gradletest-1.0/sample/test
通过后缀json得到mediaType,是json格式:http://localhost:8080/gradletest-1.0/sample/test.json

通过后缀xml得到mediaType,是xml格式:http://localhost:8080/gradletest-1.0/sample/test.xml

通过url参数得到mediaType,是json格式:http://localhost:8080/gradletest-1.0/sample/test?mediaType=json

通过url参数得到mediaType,是xml格式:http://localhost:8080/gradletest-1.0/sample/test?mediaType=jxml

后缀,url参数都有,后缀优先得到mediaType是xml格式:http://localhost:8080/gradletest-1.0/sample/test.xml?mediaType=json
   */
//  @Override  
//  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {  
//      configurer.favorPathExtension(true).useJaf(false)  
//              .favorParameter(true).parameterName("mediaType")  
//              .ignoreAcceptHeader(true).  
//              //defaultContentType(MediaType.APPLICATION_JSON).  
//              mediaType("xml", MediaType.APPLICATION_XML).  
//              mediaType("json", MediaType.APPLICATION_JSON);  
//  }  
  
//  
//  //****************spring开始配置***************************
//  
////  private @Value("#{jdbcProperties.url}") String jdbcUrl="jdbc\\:sqlite\\:c\\:/sample.db";
////  private @Value("#{jdbcProperties.username}") String username;
////  private @Value("#{jdbcProperties.password}") String password;
//

  
  @Bean
  public LocalSessionFactoryBean sessionFactory() throws IOException{
    // more configuration...
    Properties properties=new Properties();
//    properties.put("hibernate.connection.driver_class", "org.sqlite.JDBC");
    properties.put("hibernate.dialect", "algz.platform.core.configure.database.dialect.SQLiteDialect");
   // properties.setProperty("hibernate.max_fetch_depth", "3");
    //properties.setProperty("hibernate.show_sql", "false");
    //    properties.put("hibernate.connection.url", "jdbc:sqlite:c:/sample.db");
//    properties.put("hibernate.connection.username", "");
//    properties.put("hibernate.connection.password", "");
    
      LocalSessionFactoryBean factoryBean=new LocalSessionFactoryBean();
//      factoryBean.setPackagesToScan(new String[]{"algz.platform.test"});
      //  sessionFactoryBean.setPackagesToScan("com.coderli.shurnim.*.model");
      factoryBean.setHibernateProperties(properties);
      factoryBean.setDataSource(dataSource());
    //扫描model类,不然报Caused by: org.hibernate.MappingException: Unknown entity:
      factoryBean.setPackagesToScan(new String[] {"algz.platform.core.security.*"});
      factoryBean.afterPropertiesSet();
      return factoryBean;

//      LocalSessionFactoryBuilder factoryBuilder =new LocalSessionFactoryBuilder(dataSource());
////      factoryBuilder.scanPackages("algz.platform.test");
//      factoryBuilder.setProperties(properties);
////      factoryBuilder.afterPropertiesSet();
//      return (LocalSessionFactoryBean) factoryBuilder.buildSessionFactory();
  }

  @Bean
  public DataSource dataSource() {
//      //JDBC连接
  	DriverManagerDataSource dm=new DriverManagerDataSource("jdbc:sqlite:c:/sample.db","","");
    //BasicDataSource ds = new BasicDataSource();
  	dm.setDriverClassName("org.sqlite.JDBC");
  	return dm;
  }
	
  
  /*********************** Spring Security 配置 *************************/

//  
//  /**
//   * 访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
//   * @return
//   */
//  @Bean
//  public AccessDecisionManager accessDecisionManager(){
//	  return new SecurityAccessDecisionManager();
//  }
//  
//  /**
//   * 资源源数据定义，将所有的资源和权限对应关系建立起来，即定义某一资源可以被哪些角色访问
//   * @return
//   */
//  @Bean
//  public FilterInvocationSecurityMetadataSource securityMetadataSource(){
//	  return new SecurityMetadataSource();
//  }


}
