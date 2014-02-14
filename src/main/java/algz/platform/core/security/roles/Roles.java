package algz.platform.core.security.roles;

import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.Resources;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import algz.platform.core.ALGZCoreVersion;

@Entity
@Table(name="A_ROLES")
public class Roles implements GrantedAuthority {

	private static final long serialVersionUID = ALGZCoreVersion.SERIAL_VERSION_UID;
	
	@Id
	@Column
	private Integer roleid;
	
	@Column
	private String roleName;
	
	@Column
	private String enabled;
	
	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	Set<Resources> getResources(){
		return null;
	}

	@Override
	public String getAuthority() {
		return this.roleName;
	}
}
