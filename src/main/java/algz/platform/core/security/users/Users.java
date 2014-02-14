package algz.platform.core.security.users;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import algz.platform.core.ALGZCoreVersion;
import algz.platform.core.security.roles.Roles;

/**
 * 一个用户多个角色
 * @author algz
 *
 */
@Entity
@Table(name="A_USERS")
public class Users implements UserDetails {
	
	private static final long serialVersionUID = ALGZCoreVersion.SERIAL_VERSION_UID;
	
	@Id
	@Column
	private Integer userid;

	@Column
    private String username;
	
	@Column
    private String password;
	
	
    private boolean accountNonExpired;
	
	
    private boolean accountNonLocked;
	
	
    private boolean credentialsNonExpired;
	
	
    private boolean enabled;
	
//	@Formula ("(select DISTINCT RO.ROLENAME from A_USERS U INNER JOIN A_USERS_ROLES UR ON U.USERID=UR.ROLEID INNER JOIN A_ROLES RO ON RO.ROLEID=UR.ROLEID WHERE U.USERID=userid )")  
//	private int authoritie1;
    
    @Transient
	private Collection<? extends GrantedAuthority> authorities;

	/** 
     * 采用表关联映射一对多的关系 
     * @JoinTable 用于标注用于关联的表,用法与@table类似 
     * name指的是连接这两个表的关联表名称 
     * joinColumns属性表示，在保存关系中的表中，所保存关联关系的外键的字段。并配合@JoinColumn标记使用。 
     * （此处表示为表ref_school_student 中关联表school的字段 的字段school_id 外键关联school的 id） 
     *  inverseJoinColumns属性与joinColumns属性类似，它保存的是保存关系的另一个外键字段。 
     *  (此处表示表ref_school_student 中关联表student的字段 student_id 外键关联student的 字段 id) 
     * */  
    @OneToMany(targetEntity=Roles.class)  
    @JoinTable(name="A_USERS_ROLES",joinColumns={  
            @JoinColumn(name="USERID", referencedColumnName="USERID")},  
            inverseJoinColumns={  
            @JoinColumn(name="ROLEID", referencedColumnName="ROLEID")  
    })
    private List<Roles> roles;
	
    
	
//	public Users(String username, String password, boolean enabled,
//			boolean accountNonExpired, boolean credentialsNonExpired,
//			boolean accountNonLocked,
//			Collection<? extends GrantedAuthority> authorities) {
//		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
//				accountNonLocked, authorities);
//	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities=authorities;
	}
	
	
}
