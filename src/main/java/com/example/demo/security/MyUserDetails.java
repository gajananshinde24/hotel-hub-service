package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MyUserDetails implements UserDetails {
	
	  private UUID id;

	  private String username;
	  
	  private String password;
	  
	  private String role;

	  private List<? extends GrantedAuthority> authorities;
	  
	  private boolean active;
	  
	  public static MyUserDetails build(User user) {
		  	String s = user.getRole().toString();
//			String [] roles = s.split(",");
			List<SimpleGrantedAuthority> authority = new ArrayList<>(); 
		
			authority.add(new SimpleGrantedAuthority(s));
			
			
			
			System.out.println("Authority______ "+authority);
			System.out.println(user);
			return new MyUserDetails( user.getUserId(),user.getEmail(), user.getPassword(), authority, true);
		  }

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	  
	 public String getRole()
	 {
		 return role;
	 }
	 
	 public MyUserDetails(UUID id, String uid, String pwd, List<? extends GrantedAuthority> authorities, boolean active)
		{
			this.id = id;
			this.username  = uid;
			this.password = pwd;
			this.authorities = authorities;
			this.active = active;
		}
}