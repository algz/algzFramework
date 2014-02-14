package algz.platform.core.security.resources;

import java.util.List;
import java.util.Map;


public interface ResourcesDao {

	/**
	 * 加载所有资源URL
	 */
	public List<Map<String, String>> findAll();
}
