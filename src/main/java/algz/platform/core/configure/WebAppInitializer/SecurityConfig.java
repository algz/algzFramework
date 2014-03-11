package algz.platform.core.configure.WebAppInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.mvc.BaseCommandController;

import algz.platform.core.security.MyAccessDecisionManager;
import algz.platform.core.security.MySecurityFilter;
import algz.platform.core.security.MyUsernamePasswordAuthenticationFilter;
import algz.platform.core.security.SecurityAccessDecisionManager;
import algz.platform.core.security.SecurityMetadataSource;
import algz.platform.core.security.SecurityVoter;
import algz.platform.core.security.resources.ResourcesDao;
import algz.platform.core.security.resources.ResourcesDaoImpl;

@Configuration
@EnableWebMvcSecurity
//@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userService;
	
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ResourcesDao resourcesDao;
    
    @Autowired
    private SecurityMetadataSource securityMetadataSource;
    
    @Autowired
    private MyAccessDecisionManager securityAccessDecisionManager;
    
	@Autowired
	private AuthenticationManager authenticationManager; 
    
//	@Autowired
//	private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
	
//    @Autowired
//    private MySecurityFilter securityFilter;
    
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
        .jdbcAuthentication()
            .dataSource(dataSource);
            //以下语句会自动创建authorities,users表
            //.withUser("user").password("password").roles("USER").and()
            //.withDefaultSchema();
       
//    	  BindAuthenticator authenticator = new BindAuthenticator(contextSource);
//    	          authenticator.setUserDnPatterns(new String[] { "uid={0},ou=people" });
//    	          DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");
//    	          populator.setGroupRoleAttribute("cn");
//    	          populator.setGroupSearchFilter("uniqueMember={0}");
//    	          AuthenticationProvider authProvider = new LdapAuthenticationProvider(
//    	                  authenticator, populator);
//    	          auth.authenticationProvider(authProvider);

    	
//        auth
//            .inMemoryAuthentication()//.withObjectPostProcessor(objectPostProcessor)
//                .withUser("user").password("password").roles("USER") //在内存中的验证”user”用户
//                .and()
//                .withUser("admin").password("password").roles("ADMIN","USER");
    }
    
    //用于配置Authentication，比如LDAP, Database连接，以及用户和角色的查询方法。
        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {

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
      //用于配置URL的保护形式，和login页面。
    @Override
    protected void configure(HttpSecurity http) throws Exception {	
        http 
           
            //.antMatcher("/index.jsp").anonymous()
            //.and()
            .csrf().disable()
            
            .userDetailsService(userService) 
            //增加自定义的过滤器到过滤链
            .addFilter(usernamePasswordAuthenticationFilter())
            //.addFilterBefore(securityFilter, FilterSecurityInterceptor.class)
            
            .addFilterBefore(securityFilter(), FilterSecurityInterceptor.class)
            .authorizeRequests() //Allows restricting access based upon the HttpServletRequest using 
               // .accessDecisionManager(securityAccessDecisionManager)//(accessDecisionManager())
                //.antMatchers("/login","/about").anonymous()//.permitAll() // 任何人(包括没有经过验证的)都可以访问”/signup”和”/about”
//                .antMatchers("/resources/**").permitAll()
//    	        .antMatchers("/admin1/**").hasRole("ADMIN12_") // “/admin/”开头的URL必须要是管理员用户，譬如”admin”用户
//                .antMatchers("/hello1/**").hasAuthority("ADMIN11_") //使用hasRole("ADMIN12"),则对ADMIN12自动 加ROLE_前缀
    	        //.anyRequest().authenticated() //指定的URL允许任何已验证的用户访问   Specify that URLs are allowed by any authenticated user.
                .and()
            .formLogin() //使用Java配置默认值设置了基于表单的验证。使用POST提交到”/login”时，需要用”username”和”password”进行验证。
                .loginPage("/platform/login.jsp")//指定登陆页面，如果未认证访问保护资源，则跳转到此页面。
                //.loginProcessingUrl("/j_spring_security_check") // 登陆时的form action提交名，仅仅处理HTTP POST,意味着用访问”/login”时，显示登陆页面
    	        //.defaultSuccessUrl("/index.html")//登录成功后跳转到哪个页面
                //.failureUrl("/loginfailed")//登录失败时跳转到哪个页面
				.usernameParameter("username")//登陆时的用户参数名
				.passwordParameter("password")//登陆时的密码参数名
                .permitAll() //让login.html人人可访问，否则会导致递归跳转。// 任何人(包括没有经过验证的)都可以访问”/login”和”/login?error”。permitAll()是指用户可以访问formLogin()相关的任何URL。
                .and()
            .logout() //As you might expect, logout().permitAll() allows any user to request logout and view logout success URL.              
                .logoutSuccessUrl("/logout")    
                .permitAll()
                .and()
             .exceptionHandling()
                .accessDeniedPage("/platform/error/error-404.jsp")
              //  .and()
            //.httpBasic().authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
            ; //HTTP Basic Authentication is supported
    }

    
    /**
     <http security="none" pattern="/resources/**"/>
            忽略任何以”/resources/”开头的请求，这和在XML配置http@security=none的效果一样
           用于配置类似防火墙，放行某些URL。
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/**/*.js")
           .antMatchers("/**/*.css")
           .antMatchers("/**/*.html"); ; 
    }//已验证,没问题.
    
    
    
    /**
     * 访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
     * @return
     */
//      @Bean
//       public AccessDecisionManager accessDecisionManager(){
//      	 
//  	  List<AccessDecisionVoter> decisionVoters=new ArrayList<AccessDecisionVoter>();
//  	  //decisionVoters.add((AccessDecisionVoter)roleVoter());
//        //decisionVoters.add(new AuthenticatedVoter());
////  	  WebExpressionVoter webExpressionVoter= new WebExpressionVoter();
////  	  SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();
////  	  webExpressionVoter.setExpressionHandler(expressionHandler);
////        decisionVoters.add(webExpressionVoter);
//  	  
//  	    //自定义投票器,用于匹配自定义的资源数据定义
//  	    decisionVoters.add(new SecurityVoter());
//        
//        SecurityAccessDecisionManager accessDecisionManager=new SecurityAccessDecisionManager(decisionVoters);
//        // 该属性默认值为false ,即没有显式定义的资源都保护起来。
//        accessDecisionManager.setAllowIfAllAbstainDecisions(false);
//        return accessDecisionManager;//new SecurityAccessDecisionManager();
//    }
    
    /**
     * 授权管理器(调用SpringSecurity的授权管理器提供给自定义的过滤器用)
     */
    @Bean
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 资源源数据定义，将所有的资源和权限对应关系建立起来，即定义某一资源可以被哪些角色访问
     * @return
     */
//    @Bean
//    public FilterInvocationSecurityMetadataSource securityMetadataSource(){
//  	  return new SecurityMetadataSource(this.resourcesDao);
//    }
    
    /**
     * 自定义一个过滤器并赋值自定义的决策器,授权管理器,资源源数据定义.
     * 
     *  SpringSecurity的过滤器链调用类:public class FilterChainProxy extends GenericFilterBean 
     * 
     * 由于不能直接对SpringSecurity的过滤器FilterSecurityInterceptor赋值相关参数,
     * 所以只能重新创建一个FilterSecurityInterceptor过滤器赋给自定义参数后,
     * 把自定义的过滤器加入到SpringSecurity的FilterSecurityInterceptor过滤器前,
     * 因为SpringSecurity的FilterSecurityInterceptor具有防重调用功能,
     * 所以执行完自定义的过滤器FilterSecurityInterceptor后就不会在执行自已的过滤器FilterSecurityInterceptor了.
     * @return
     */
    @Bean
    public FilterSecurityInterceptor securityFilter(){
  	  FilterSecurityInterceptor filter=new FilterSecurityInterceptor();
  	  filter.setAccessDecisionManager(securityAccessDecisionManager);//(accessDecisionManager());
  	  filter.setSecurityMetadataSource(securityMetadataSource);
  	  try {
  		filter.setAuthenticationManager(authenticationManager);//(authenticationManagerBean());
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  	  return filter;
    }
    
    /**
     *     <!-- 登录验证器 -->  
    <beans:bean id="loginFilter"  
        class="com.huaxin.security.MyUsernamePasswordAuthenticationFilter">  
        <!-- 处理登录的action -->  
        <beans:property name="filterProcessesUrl" value="/j_spring_security_check"></beans:property>  
                <!-- 验证成功后的处理-->  
        <beans:property name="authenticationSuccessHandler" ref="loginLogAuthenticationSuccessHandler"></beans:property>  
                <!-- 验证失败后的处理-->  
        <beans:property name="authenticationFailureHandler" ref="simpleUrlAuthenticationFailureHandler"></beans:property>  
        <beans:property name="authenticationManager" ref="myAuthenticationManager"></beans:property>  
        <!-- 注入DAO为了查询相应的用户 -->  
        <beans:property name="usersDao" ref="usersDao"></beans:property>  
    </beans:bean>  
     * @return
     */
    @Bean
    public MyUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
    	MyUsernamePasswordAuthenticationFilter filter=new MyUsernamePasswordAuthenticationFilter();
    	//处理登录的action
    	filter.setFilterProcessesUrl("/j_spring_security_check");
  	    //验证成功后的处理
  	    filter.setAuthenticationSuccessHandler(loginLogAuthenticationSuccessHandler());
  	    //验证失败后的处理
  	    filter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
  	    filter.setAuthenticationManager(authenticationManager);
  	    
    	return filter;
    }
    
    /**
     * 为了在未登陆的时候，跳转到哪个页面，不配也会抛异常。
     * @return
     */
    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
    	return new LoginUrlAuthenticationEntryPoint("/platform/login.jsp");
    }
    

    /**
     * 验证成功后的处理
     *     <beans:bean id="loginLogAuthenticationSuccessHandler"  
            class="org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler">  
            <beans:property name="defaultTargetUrl" value="/index.jsp"></beans:property>  
        </beans:bean> 
     * @return
     */
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginLogAuthenticationSuccessHandler(){
    	SavedRequestAwareAuthenticationSuccessHandler handler= new SavedRequestAwareAuthenticationSuccessHandler();
    	handler.setDefaultTargetUrl("/platform/index.jsp");
    	return handler;
    }   
    
    /**
     * 验证失败后的处理
    <beans:bean id="simpleUrlAuthenticationFailureHandler"  
            class="org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler">  
            <!-- 可以配置相应的跳转方式。属性forwardToDestination为true采用forward false为sendRedirect -->  
            <beans:property name="defaultFailureUrl" value="/login.jsp"></beans:property>  
        </beans:bean>  
     * @return
     */
    @Bean
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler(){
    	SimpleUrlAuthenticationFailureHandler handler= new SimpleUrlAuthenticationFailureHandler();
    	handler.setDefaultFailureUrl("/platform/login.jsp?error=1");
    	return handler;
    } 

}
