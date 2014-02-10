package algz.platform.core.security.resources;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import algz.platform.core.ALGZCoreVersion;

@Entity
@Table(name="A_RESOURCES")
public class Resources implements Serializable {

	private static final long serialVersionUID = ALGZCoreVersion.SERIAL_VERSION_UID;
	
	@Column
	private BigDecimal resourceid;
	
	@Column
	private String resourceName;
	
	@Column
	private String url;

	public BigDecimal getRecourcesid() {
		return resourceid;
	}

	public void setRecourcesid(BigDecimal resourceid) {
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
