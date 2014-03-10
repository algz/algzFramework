package com.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import algz.platform.core.security.resources.ResourcesDao;
import algz.platform.core.security.users.Users;

@Repository
public class HelloDaoImpl implements HelloDao {
	
	@Autowired
	private SessionFactory sf;

	
	/* (non-Javadoc)
	 * @see algz.platform.test.SQLiteDao#inputDate()
	 */
	public String inputDate(){
    	Session s=sf.openSession();//
    //	s.getTransaction().begin();
    	Object[] objs=(Object[])s.createSQLQuery("select * from a_users").setMaxResults(1).uniqueResult();
    	//System.out.println("DAO:"+objs[0]);
        s.createSQLQuery("insert into a_users (username,password) values ('name211111111','1')").executeUpdate();
//        s.getTransaction().rollback();
     //   s.getTransaction().commit();
        Users u=new Users();
        u.setUsername("aaa");
        u.setPassword("a");
        s.save(u);
        return "";
	}
}
