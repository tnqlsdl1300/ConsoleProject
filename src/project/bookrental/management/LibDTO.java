package project.bookrental.management;

import java.io.Serializable;

// 사서 DTO
public class LibDTO implements Serializable{

	private static final long serialVersionUID = -390745070057031455L;
	private String libId;			// 사서 id
	private String libPwd;		// 사서 pwd
	
	public LibDTO() {		}

	public LibDTO(String libId, String libPwd) {
		super();
		this.libId = libId;
		this.libPwd = libPwd;
	}

	public String getLibId() {
		return libId;
	}

	public void setLibId(String libId) {
		this.libId = libId;
	}

	public String getLibPwd() {
		return libPwd;
	}

	public void setLibPwd(String libPwd) {
		this.libPwd = libPwd;
	}
	
	
	
}
