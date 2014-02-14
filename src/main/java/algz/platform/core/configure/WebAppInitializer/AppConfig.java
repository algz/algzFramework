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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import algz.platform.core.security.SecurityAccessDecisionManager;
import algz.platform.core.security.SecurityFilter;
import algz.platform.core.security.SecurityMetadataSource;
import algz.platform.core.security.SecurityVoter;
import algz.platform.core.security.resources.ResourcesDao;

/**
 * @EnableWebMvc导入spring_mvc需要的诸多bean，再配合@ComponentScan扫描包里面所有@Component(@Repository @Service  @Constroller)，基本的mvc配置就完成了。
 * @EnableTransactionManagement  //启动事务管理配置
* @ImportResource导入基于XML方式的配置文件，多个配置文件采{"",""}数组。
* 
* @author algz
* Spring Config配置(无XML)
*/
@Configuration
//@ImportResource({"classpath:algz/platform/config/xml/SpringSecurity-context.xml"})
@ComponentScan(basePackages = {"algz.platform.core","com"})//扫描注解组件的包的基础位置(@Controller,@Service...)
@EnableWebMvc
@EnableTransactionManagement  //声明式事务管理，通过spring root application context扫描包septem.config.app：
//@PropertySource("/conf/jdbc.properties")
@Order(1)
public class AppConfig {

	
/* other version of the same beans - no autowiring, CGLIB magic only

  @Bean
  public EntityManagerFactory entityManagerFactory() {

      LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
      entityManagerFactory.setDataSource(dataSource());
      entityManagerFactory.setPackagesToScan(new String[] { "jpa.config.java" });
      entityManagerFactory.setPersistenceProvider(new HibernatePersistence());
      entityManagerFactory.afterPropertiesSet();
      return entityManagerFactory.getObject();
  }

  @Bean
  @Autowired
  public PlatformTransactionManager transactionManager() {
      JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());
      transactionManager.setDataSource(dataSource());
      transactionManager.setJpaDialect(new HibernateJpaDialect());
      return transactionManager;
  }

  @Bean
  public DataSource dataSource() {
      EmbeddedDatabaseFactoryBean bean = new EmbeddedDatabaseFactoryBean();
      ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
      databasePopulator.addScript(new ClassPathResource("hibernate/config/java/schema.sql"));
      bean.setDatabasePopulator(databasePopulator);
      bean.afterPropertiesSet(); // necessary because
                                  // EmbeddedDatabaseFactoryBean is a
                                  // FactoryBean
      return bean.getObject();
  }

*/

	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		return  new HibernateTransactionManager(sessionFactory().getObject());
	}


//  //*********************springmvc开始配置*************************
//  /**
//   * Resolve logical view names to .jsp resource in the /WEB-INF/views directory
//   * 解析所有视图为前缀(/WEB-INF/views)+视图名称+后缀(.jsp)的文件.
//   springmvc XML:
//       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
//        <property name="prefix">WEB-INF/views/</property>
//        <property name="suffix">.jsp</property>
//      </bean>
//   */
  @Bean
  public ViewResolver viewResolver(){
      InternalResourceViewResolver resolver =new InternalResourceViewResolver();
      resolver.setPrefix("/WEB-INF/pages/");
      resolver.setSuffix(".jsp");
      return resolver;
  }
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
