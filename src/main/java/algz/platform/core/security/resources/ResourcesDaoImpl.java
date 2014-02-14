package algz.platform.core.security.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import algz.platform.core.security.roles.Roles;
import algz.platform.core.security.users.Users;


/**
 * 
 * @author algz
 *
 */
@Repository
public class ResourcesDaoImpl implements ResourcesDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/*
	 * (non-Javadoc)
	 * @see algz.platform.core.security.resources.ResourcesDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll() {	
//		String sql1="select * from A_USERS u where u.USERNAME='user01'";
//		Users user= (Users)sessionFactory.openSession().createSQLQuery(sql1)
//		              .addEntity(Users.class).uniqueResult();
//		List<Roles> l=user.getRoles();
//		if(l.size()!=0){
//			Roles r=l.get(0);
//			System.out.println(r.getRoleid());
//		}
		
		String sql="SELECT distinct RES.URL,ROL.ROLENAME FROM A_RESOURCES RES " +
				"INNER JOIN A_ROLES_RESOURCES RRES ON RRES.RESOURCEID=RES.RESOURCEID " +
				"INNER JOIN A_ROLES ROL ON ROL.ROLEID=RRES.ROLEID";
		List<Map<String, String>> listMap=sessionFactory.openSession().createSQLQuery(sql)
				                           .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				                           .list();
		return listMap;
	}

}
