package algz.platform.core.security;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;


import algz.platform.core.security.resources.Resources;
import algz.platform.core.security.resources.ResourcesDao;

/**
 * 自定义资源的访问权限的定义加载器
 * @author algz
 *
 */
@Service
public class SecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	
    /*
     * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
     * 
     */
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
    
	@Autowired
	private ResourcesDao resourcesDao;
	
	// 由spring调用 
//	public SecurityMetadataSource(ResourcesDao resourcesDao) {
//		this.resourcesDao=resourcesDao;
//		System.out.println("正在加载所有资源和权限关系...");
//        loadResourceDefine();
//	}

	/**
	 * 返回请求资源URL所对应的权限
	 * @param object 请求的资源URL
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getHttpRequest();
     
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if (requestMatcher.matches(request)) {
                requestMatcher = null;
                System.out.println("返回请求资源URL:"+request.getServletPath()+"所对应的权限:"+resourceMap.get(resURL));
                return resourceMap.get(resURL);
            }
        }
        return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	/**
	 * @PostConstruct是Java EE 5引入的注解，
	 * Spring允许开发者在受管Bean中使用它。当DI容器实例化当前受管Bean时，
	 * @PostConstruct注解的方法会被自动触发，从而完成一些初始化工作，
	 * 
	 * //加载所有资源与权限的关系
	 */
	@PostConstruct
	private void loadResourceDefine() {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		List<Map<String, String>> listMap = this.resourcesDao.findAll();
		for (Map<String, String> map : listMap) {
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			// 以权限名封装为Spring的security Object
			ConfigAttribute configAttribute = new SecurityConfig(
					map.get("ROLENAME"));
			configAttributes.add(configAttribute);
			resourceMap.put(map.get("URL"), configAttributes);
		}
		System.out.println("加载完数据库中的所有资源和权限关系...");
	} 
	
}
