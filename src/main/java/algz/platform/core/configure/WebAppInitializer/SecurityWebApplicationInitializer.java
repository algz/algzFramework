package algz.platform.core.configure.WebAppInitializer;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Overriding the default configure(HttpSecurity) method
 * 父类 AbstractSecurityWebApplicationInitializer 实现 WebApplicationInitializer 接口,
 * 并设置 HttpSecurity 默认值.
 * @author algz
 *
 web.xml设置
<filter>
  <filter-name>springSecurityFilterChain</filter-name>
  <filter-class>
    org.springframework.web.filter.DelegatingFilterProxy
  </filter-class>
</filter>

<filter-mapping>
  <filter-name>springSecurityFilterChain</filter-name>
  <url-pattern>/*</url-pattern>
  <dispatcher>ERROR</dispatcher>
  <dispatcher>REQUEST</dispatcher>
</filter-mapping>
 *
 */
public class SecurityWebApplicationInitializer extends
		AbstractSecurityWebApplicationInitializer {
}
