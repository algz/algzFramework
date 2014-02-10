package algz.platform.core.security.users;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import algz.platform.core.ALGZCoreVersion;
import algz.platform.core.security.roles.Roles;

@Entity
@Table(name="A_USERS")
public class Users extends User {
	
	private static final long serialVersionUID = ALGZCoreVersion.SERIAL_VERSION_UID;
	
	@Column
	private BigDecimal userid;

	@Column
    private String username;
	
	@Column
    private String password;
	
	@Column
    private boolean accountNonExpired;
	
	@Column
    private boolean accountNonLocked;
	
	@Column
    private boolean credentialsNonExpired;
	
	@Column
    private boolean enabled;
	
    private Set<GrantedAuthority> authorities;
	
	public Users(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}



	

	public List<Roles> getRoles(){
		return null;
	}
	
}
