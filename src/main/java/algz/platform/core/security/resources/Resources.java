package algz.platform.core.security.resources;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import algz.platform.core.ALGZCoreVersion;


/**
 * 一个资源对应多个角色或用户
 * @author algz
 *
 */
@Entity
@Table(name="A_RESOURCES")
public class Resources implements Serializable {

	private static final long serialVersionUID = ALGZCoreVersion.SERIAL_VERSION_UID;
	
	@Id
	@Column
	private Integer resourceid;//sqlite 不支持BigDecimal
	
	@Column
	private String resourceName;
	
	@Column
	private String url;

	public Integer getRecourcesid() {
		return resourceid;
	}

	public void setRecourcesid(Integer resourceid) {
		this.resourceid = resourceid;
	}
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
