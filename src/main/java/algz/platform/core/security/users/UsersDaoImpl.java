package algz.platform.core.security.users;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import algz.platform.core.security.roles.Roles;

@Repository
public class UsersDaoImpl implements UsersDao{

	@Autowired
	private SessionFactory sessionFactory;


	public Users findUserByUserid(String userid) {
		String sql="select * from A_USERS u where u.USERNAME='"+userid+"'";
		Users user= (Users)sessionFactory.openSession().createSQLQuery(sql)
		              .addEntity(Users.class).uniqueResult();
		 return user;
	}

	@Override
	public Roles getRoles(String userid) {
		// TODO Auto-generated method stub
		return null;
	}
}
