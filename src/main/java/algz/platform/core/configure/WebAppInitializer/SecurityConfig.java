package algz.platform.core.configure.WebAppInitializer;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import algz.platform.core.security.SecurityAccessDecisionManager;
import algz.platform.core.security.SecurityFilter;
import algz.platform.core.security.SecurityMetadataSource;
import algz.platform.core.security.resources.ResourcesDao;
import algz.platform.core.security.resources.ResourcesDaoImpl;


@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userService;
	
    @Autowired
    private DataSource dataSource;
	
	/**
<authentication-manager>
  <authentication-provider>
    <user-service>
      <user name="user" 
          password="password" 
          authorities="ROLE_USER"/>
      <user name="admin" 
          password="password" 
          authorities="ROLE_USER,ROLE_ADMIN"/>
    </user-service>
  </authentication-provider>
</authentication-manager>
	 * */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
          .userDetailsService(userService);
//        .jdbcAuthentication()
//            .dataSource(dataSource)
//            .withDefaultSchema()
//            .withUser("user").password("password").roles("USER");
        
//        auth
//            .inMemoryAuthentication()//.withObjectPostProcessor(objectPostProcessor)
//                .withUser("user").password("password").roles("USER") //在内存中的验证”user”用户
//                .and()
//                .withUser("admin").password("password").roles("ADMIN","USER");
    }
    
    /**
     * 
<http use-expressions="true">
  <intercept-url pattern="/logout" access="permitAll"/>
  <intercept-url pattern="/login" access="permitAll"/>
  <intercept-url pattern="/signup" access="permitAll"/>
  <intercept-url pattern="/about" access="permitAll"/>
  <intercept-url pattern="/**" access="hasRole('ROLE_USER')"/>
  <logout
      logout-success-url="/login?logout"
      logout-url="/logout"
  />
  <form-login
      authentication-failure-url="/login?error"
      login-page="/login"
      login-processing-url="/login"
      password-parameter="password"
      username-parameter="username"
  />
</http>
     * 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	SecurityFilter filter=new SecurityFilter();
  	    filter.setAccessDecisionManager(accessDecisionManager());
  	    filter.setSecurityMetadataSource(securityMetadataSource());
  		filter.setAuthenticationManager(this.authenticationManager());
        http
            .addFilterBefore(filter, FilterSecurityInterceptor.class)
            .authorizeRequests()
                .antMatchers("/signup","/about").permitAll() // 任何人(包括没有经过验证的)都可以访问”/signup”和”/about”
    	        .antMatchers("/admin/**").hasRole("ADMIN") // “/admin/”开头的URL必须要是管理员用户，譬如”admin”用户
                .anyRequest().authenticated() //所有其他的URL都需要用户进行验证
                .and()
            .formLogin() //使用Java配置默认值设置了基于表单的验证。使用POST提交到”/login”时，需要用”username”和”password”进行验证。
                //.loginPage("/crm/signin.html")
                .loginProcessingUrl("/login") // 注明了登陆页面，意味着用GET访问”/login”时，显示登陆页面
    	        .defaultSuccessUrl("/crm/welcome.html")
    	        .failureUrl("/crm/signin.html?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
                .permitAll() // 任何人(包括没有经过验证的)都可以访问”/login”和”/login?error”。permitAll()是指用户可以访问formLogin()相关的任何URL。
                .and()
            .logout() //As you might expect, logout().permitAll() allows any user to request logout and view logout success URL.              
                .permitAll()
                .and()
            .rememberMe()
                    .userDetailsService(userService);
            //.httpBasic(); //HTTP Basic Authentication is supported
    }

    
    /**
     <http security="none" pattern="/resources/**"/>
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           //忽略任何以”/resources/”开头的请求，这和在XML配置http@security=none的效果一样
           .antMatchers("/resources/**"); // <http security="none" pattern="/resources/**"/>
    }
    
    
//  @Bean
//  public Filter securityFilter(){
//	  SecurityFilter filter=new SecurityFilter();
//	  filter.setAccessDecisionManager(accessDecisionManager());
//	  filter.setSecurityMetadataSource(securityMetadataSource());
//	  try {
//		filter.setAuthenticationManager(this.authenticationManager());
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	  return filter;
//  }
//  
  /**
   * 访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
   * @return
   */
  @Bean
  public AccessDecisionManager accessDecisionManager(){
	  return new SecurityAccessDecisionManager();
  }
  
  /**
   * 资源源数据定义，将所有的资源和权限对应关系建立起来，即定义某一资源可以被哪些角色访问
   * @return
   */
  @Bean
  public FilterInvocationSecurityMetadataSource securityMetadataSource(){
	  return new SecurityMetadataSource();
  }
  

}
