package algz.platform.core.security.resources;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResourcesDaoImpl implements ResourcesDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Resources> findAll() {
		return new ArrayList();
	}

}
