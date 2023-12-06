package user;

import java.io.Serializable;

import main.Password;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String name;
	private boolean darkMode;
	
	private Password p;
	
	public User() {
	}
	
	public User(String name, String email, String passwd, boolean dark) {
		this.email = email;
		this.name = name;
		this.p = new Password(passwd);
		this.darkMode = dark;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPassword(Password passwd) {
		this.p = passwd;
	}
	
	public Password getPassword() {
		return p;
	}
	
	public void setDarkMode(boolean dark) {
		this.darkMode = dark;
	}
	
	public boolean darkMode() {
		return this.darkMode;
	}
}
