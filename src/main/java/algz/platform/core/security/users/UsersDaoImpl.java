package algz.platform.core.security.users;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import algz.platform.core.security.roles.Roles;

@Repository
public class UsersDaoImpl implements UsersDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Users findUserByUserid(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Roles getRoles(String userid) {
		// TODO Auto-generated method stub
		return null;
	}
}
