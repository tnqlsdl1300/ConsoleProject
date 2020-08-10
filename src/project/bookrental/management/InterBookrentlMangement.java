package project.bookrental.management;

import java.util.Scanner;

public interface InterBookrentlMangement {
	
	///////////////////////////////////////////////////////////////////////////////////////
	// 사서 interface
	void libSignIN(Scanner sc);   // 사서 회원가입
	// 1. 입력받은 Scanner 값을 사용하여 사서List 파일생성
	
	LibDTO libLogin(Scanner sc); // 사서로그인
	// 1. 입력받은 Scanner 값과 객체의 id, pw가 동일할 경우 Next
	// 2. 입력받은 id,pw와 같은 값을 가지고 있는 객체를 반환
	
	void libMenu(Scanner sc); // 사서메뉴
	// 1. 사서로그인 메서드에서 반환된 libDTO 객체를 사용  (ex> libDTO.getName() 님이 로그인 하셨습니다)
	// 2. 스캐너를 사용하여 입력받는 숫자에 따라 필요 메소드 호출
	
	boolean isUseLibId(String id); // 사서 로그인 id 유효성 검사
	// 1. libLogin 메소드에서 반환된 libDTO를 입력받음.
	
	boolean isUseUserId(String id);	// 유저 로그인 id 유효성 검사
	
	boolean isUseBookId(String id);	// 책 고유 아이디 유효성 검사
	
	void deleteUser(Scanner sc);	// 회원 삭제 메서드
	
	void showUser(Scanner sc);	// 회원 정보 검색 메서드(기본 정보, 대여했는지)
	
	void ISBNRegist(Scanner sc); // 책등록(ISBN)
	
	void bookRegist(Scanner sc);  // 책등록(책 고유번호 - book ID)
	
	void bookRental(Scanner sc);  // 책등록 (ISBN의 객체에서 isUse(대여상태)를 바꾼후 파일 저장)
	// 1. 회원의 ID를 입력받는다
	
	// 2. 회원의 ID를 사용하여 회원 DTO에 접근하여 해당 회원이 존재할 경우 NEXT
	// 2-1. 회원이 존재하지 않을경우 거부
	// 2-2. 회원이 반납기한을 넘긴 책이 존재할 경우 거부.
	
	// 3. 총 대여권수를 입력받는다. (해당 숫자만큼(정수) bookID(책 고유번호)를 입력받는다)
	// 3-1. 입력받은 책의 고유번호가 존재하는지 if문을 사용하여 ISBN.get(i).getbookIdDTO() 에 접근하여
	//      해당 고유넘버가 존재할 경우 입력받은 객체들의 isUse(대여상태 값을) 변환한다.
	
	void rentedBook();  // ISBN 객체에 접근하여 isUse(대여상태)값이 false인 객체출력
	
	void bookReturn(Scanner sc);  // 책반납
	// 1. 반납할 책의 숫자를 입력받는다.
	// for문을 사용 반납하는 책의 수만큼
	// 2. Scanner로 입력받은 Book 객체로 접근하여 isUse(대여상태)를 변경
	// 3. ISBN객체에 접근하여 대여 일을 확인하여 - 현재 일을 calendar로 받는다.
	// 3-1. 현재일 - 반납일 = 연체일 * 100원 을 출력한다.
	// 4. 연체료를 전부 더해서 총 연체료를 출력받는다.
	
	///////////////////////////////////////////////////////////////////////////////////////
	// 일반사용자(User) interface
	void UserSignIn(Scanner sc); // 유저 회원가입
	// 1. 입력받은 Scanner 값을 사용하여 유저List 파일생성
	
	UserDTO UserLogin(Scanner sc);
	// 1. 입력받은 Scanner 값과 객체의 id, pw가 동일할 경우 Next
	// 2. 입력받은 id,pw와 같은 값을 가지고 있는 객체를 반환
	
	void UserMenu(Scanner sc); // 사서메뉴
	// 1. 유저로그인 메서드에서 반환된 UserDTO 객체를 사용 (ex> UserDTO.getName() 님이 로그인 하셨습니다)
	// 2. 스캐너를 사용하여 입력받는 숫자에 따라 필요 메소드 호출
	
	void BookSearch(Scanner sc); // 도서검색
	// 1. 스캐너 값을 입력받음(카테고리, 도서명, 작가명, 출판사명)
	// 2. 입력받은 값을 if문을 사용하여 ISBN 객체에서 찾는다.
	// 3. 만약 입력받은 값이 null 이거나 공백일 경우 모든 책을 선택한다.
	
	void showMyRent(UserDTO user);
	// 1. 로그인 할때 사용한 유저 객체를 그대로 사용
	// 2. 유저객체의 내부에 저장된 ISBN 객체를 출력하게 만든다.
}
