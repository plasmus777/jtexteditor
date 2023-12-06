package main;

import java.io.Serializable;

public class Password implements Serializable{

	private static final long serialVersionUID = 1L;
	private String hash;
	private String salt;
	
	public Password(String passwd) {
		salt = PasswordUtils.generateSalt(512).get();
		setHash(PasswordUtils.hashPassword(passwd, salt).get());
	}

	public String getHash() {
		return hash;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
