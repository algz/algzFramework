package algz.platform.core.security.users;

import algz.platform.core.security.roles.Roles;


public interface UsersDao {

	public Users findUserByUserid(String userid);
	//@Secured({ "ROLE_BRAND_ADMIN" })   
	public Roles getRoles(String userid);
}
