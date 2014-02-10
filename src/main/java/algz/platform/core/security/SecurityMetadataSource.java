package algz.platform.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import algz.platform.core.security.resources.Resources;
import algz.platform.core.security.resources.ResourcesDao;

public class SecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	
    /*
     * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
     * sparta
     */
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	// 由spring调用
	public SecurityMetadataSource() { 
        //loadResourceDefine();
	}

	/**
	 * 返回所请求资源所需要的权限
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		
        // object 是一个URL，被用户请求的url。
        String url = ((FilterInvocation) object).getRequestUrl();
        System.out.println("requestUrl is " + url);
		if (resourceMap == null) {
			loadResourceDefine();
		}
        int firstQuestionMarkIndex = url.indexOf("?");

        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }

        Iterator<String> ite = resourceMap.keySet().iterator();

        while (ite.hasNext()) {
            String resURL = ite.next();

            if (url.equals(resURL)) {

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

    //加载所有资源与权限的关系  
    private void loadResourceDefine() {  
        if(resourceMap == null) {  
//        	ApplicationContext context = new ClassPathXmlApplicationContext(
//        			"classpath:applicationContext.xml");
//
//        			SessionFactory sessionFactory = (SessionFactory) context
//        			.getBean("sessionFactory");
        			
            resourceMap = new HashMap<String, Collection<ConfigAttribute>>();  
            List<Resources> resources = new ArrayList();//this.resourcesDao.findAll();  
            for (Resources resource : resources) {  
                Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();  
                 //以权限名封装为Spring的security Object  
                ConfigAttribute configAttribute = new SecurityConfig(resource.getResourceName());  
                configAttributes.add(configAttribute);  
                resourceMap.put(resource.getUrl(), configAttributes);  
            }  
        }  
          
       // Set<Entry<String, Collection<ConfigAttribute>>> resourceSet = resourceMap.entrySet();  
       // Iterator<Entry<String, Collection<ConfigAttribute>>> iterator = resourceSet.iterator();  
          
    } 
	
}
