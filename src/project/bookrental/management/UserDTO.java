package project.bookrental.management;

import java.io.Serializable;

// 일반 회원 DTO
public class UserDTO implements Serializable{

	private static final long serialVersionUID = -7389620131679741311L;
	private String userId;			// 회원 ID
	private String userPwd;			// 회원 PWD
	private String userName;		// 회원 이름
	private String userPhone;		// 회원 전화번호
	
	private BookIdDTO bookIdDto;		// 고유 책 dto
	
	public UserDTO() {	}

	public UserDTO(String userId, String userPwd, String userName, String userPhone, BookIdDTO bookIdDto) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.userPhone = userPhone;
		this.bookIdDto = bookIdDto;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public BookIdDTO getBookIdDto() {
		return bookIdDto;
	}

	public void setBookIdDto(BookIdDTO bookIdDto) {
		this.bookIdDto = bookIdDto;
	}

	
	
	
	
}
