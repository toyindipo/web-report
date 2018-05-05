package com.activedge.report.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = User.class)
public class User implements Serializable {
	
	private static final long serialVersionUID = 1799495484442765389L;

	@Transient
	private Timestamp now = new Timestamp(System.currentTimeMillis());
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@NotNull
	@Size(min=2, max=30)
	@Column(name="first_name")
	private String firstName;
	
	@NotNull
	@Size(min=2, max=30)
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="email", unique=true)
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Size(min=6)
	@Column(name="password")
	private String password;
	
	@NotNull
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

	
	@Column(name="last_login")
	private Timestamp lastLogin = now;
	
	@Column(name="created_at")
	private Timestamp createdAt = now;
	
	@Column(name="updated_at")
	private Timestamp updatedAt = now;
	
	@Column(name="enabled")
	private Boolean enabled = Boolean.TRUE;
	
	public User() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
    public boolean equals(Object that) {
        if (!(that instanceof User)) {
            return false;
        }
        User user = (User)that;
        if (user.id == null || user.email == null) {
            return false;
        }
        return user.id.equals(id) && user.email.equals(email);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.intValue());
        result = 31 * result + (email == null ? 0 : email.hashCode());
        return result;
    }
	
}	
