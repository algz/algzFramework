package algz.platform.core.security.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UsersDao usersDao;
	
	@Override
	public UserDetails loadUserByUsername(String userid)
			throws UsernameNotFoundException {
		Users user=usersDao.findUserByUserid(userid);
      boolean enables = true;
      boolean accountNonExpired = true;
      boolean credentialsNonExpired = true;
      boolean accountNonLocked = true;
      //取得用户的权限
      List<String> roles = new ArrayList();
      roles.add("ADMIN1");
      Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();  
      for (String role : roles) {  
          SimpleGrantedAuthority simpleAuth = new SimpleGrantedAuthority(role);  
          authorities.add(simpleAuth);  
      }
      User user1 = new User(userid, "1",enables, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities); 
        if(user1==null){
        	throw new UsernameNotFoundException("用户名不存在");
        }
        

//        boolean enables = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
        //UserDetails user = new User(userid, u.getPassword(),enables, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities); 
		return user1;
	}

	
	
}
