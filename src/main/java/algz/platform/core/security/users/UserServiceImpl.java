package algz.platform.core.security.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import algz.platform.core.security.roles.Roles;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UsersDao usersDao;
	
	@Override
	public UserDetails loadUserByUsername(String userid)
			throws UsernameNotFoundException {
		Users user=usersDao.findUserByUserid(userid);
        if(user==null){
        	throw new UsernameNotFoundException("用户名不存在");
        }
      //取得登录用户的所有权限

      List<Roles> roleList=user.getRoles();
      Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();  
      for(Roles r:roleList){
    	  String role=r.getRoleName();
          SimpleGrantedAuthority simpleAuth = new SimpleGrantedAuthority(role);  
          authorities.add(simpleAuth);
    	  System.out.println("加载用户"+userid+"的所有权限:"+role);
      }
      user.setAuthorities(authorities);
      return user;
	}

	
	
}
