package algz.platform.core.security.users;

import algz.platform.core.security.roles.Roles;


public interface UsersDao {

	public Users findUserByUserid(String userid);
	
	public Roles getRoles(String userid);
}
