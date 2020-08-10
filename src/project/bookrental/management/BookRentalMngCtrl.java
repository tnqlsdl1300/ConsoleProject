package project.bookrental.management;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookRentalMngCtrl implements InterBookrentlMangement {

	// ISBN_DTO가 담긴 ArrayList가 저장 될 파일경로 및 이름 지정
	private final String ISBNLISTFILENAME = "C:/iotestdata/project/bookmng/isbnlist.dat"; // isbn(공통된 책 정보)
	private final String BOOKIDLISTFILENAME = "C:/iotestdata/project/bookmng/bookIdlist.dat"; // bookid(고유의 책 id)
	private final String LIBLISTFILENAME = "C:/iotestdata/project/bookmng/liblist.dat"; // 사서 정보(로그인 데이터)
	private final String USERLISTFILENAME = "C:/iotestdata/project/bookmng/userlist.dat"; // 유저 정보(로그인 데이터)
	private final String RENTALLISTFILENAME = "C:/iotestdata/project/bookmng/rentallist.dat"; // 도서 대여 정보(대여날짜 등 dto)

	private IsbnMngSerializable serial = new IsbnMngSerializable();

	// ISBN 유효성 검사 메서드
	@SuppressWarnings("unchecked")
	public boolean isUseISBN(String isbn) {

		// 파일로부터 모든 회원의 정보 데이터를 받아서 ibsnListObj에 저장 (Object 형식)
		Object ibsnListObj = serial.getObjectFromFile(ISBNLISTFILENAME);

		if (ibsnListObj != null) { // 경로에 파일이 존재할 때
			List<ISBNDTO> isbnList = (ArrayList<ISBNDTO>) ibsnListObj; // 파일로부터 모든 회원의 정보 데이터를 받아서 isbnList에 저장
																		// (ArrayList 형식)

			// isbn이 겹치는지 확인
			for (ISBNDTO list : isbnList) {
				if (list.getBookISBN().equals(isbn)) { // isbn 겹치는 경우 => 이미 DB에 저장된 데이터라는 뜻
					return true; // isbn이 겹치면 true 반환
				}
			}

		}

		return false; // isbn이 겹치지 않으면 false 반환
	}

	// 4. 도서정보등록 메서드
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void ISBNRegist(Scanner sc) {

		System.out.println("\n== 도서정보 등록하기==");

		// 책 정보 입력받음 start --------------------------------------
		String isbn = null;
		String price = null;
		do {

			System.out.print("> 국제표준도서번호(ISBN): ");
			isbn = sc.nextLine();
			if (!isUseISBN(isbn)) { // 겹치지 않을 시 (true값 리턴받음)
				break; // while문 탈출 밑으로
			} else {
				System.out.println("~~~ " + isbn + "는 이미 존재하므로 다른 국제표준도서번호(ISBN)를 입력하세요!!\n");
			}
		} while (true);

		// 카테고리 선언 및 입력받기
		System.out.print("> 도서분류카테고리: ");
		String category = sc.nextLine();

		// 도서명 선언 및 입력받기
		System.out.print("> 도서명: ");
		String bookName = sc.nextLine();

		// 작가명 선언 및 입력받기
		System.out.print("> 작가명: ");
		String author = sc.nextLine();

		// 출판사 선언 및 입력받기
		System.out.print("> 출판사: ");
		String company = sc.nextLine();

		// 가격 선언 및 입력받기

		// 가격이 숫자가 아닐 시 다시 입력받게 하는 do while문
		do {
			System.out.print("> 가격: ");
			price = sc.nextLine();

			try {
				int nPrice = Integer.parseInt(price); // String을 int로 변환해 예외검사를 해서 숫자/문자 구분
			} catch (NumberFormatException e) {
				System.out.println("~~~~ 오류: 가격은 숫자로만 입력하세요!!");
				continue;
			}
			break;
		} while (true);
		// 책 정보 입력받음 end --------------------------------------

		List<ISBNDTO> isbnList = null; // 파일로 보내줄 isbnList 생성
		ISBNDTO isbnDTO = new ISBNDTO(isbn, category, bookName, author, company, price); // isbnDto 객체에 입력받은 값을 대입

		// File exist()메서드를 사용해 ISBNLISTFILENAME 경로에 파일이 존재하는지 검사
		File file = new File(ISBNLISTFILENAME);

		// 경로에 파일자체가 존재하는지 검사
		if (!file.exists()) { // 파일이 존재하지 않는 경우
			isbnList = new ArrayList<>();
			isbnList.add(isbnDTO);

		} else { // 파일이 존재하는 경우

			// 이미 있는 파일에 받은 정보를 뒤에 더 추가해주기 위해 기존 파일을 불러옴
			Object isbnListObj = serial.getObjectFromFile(ISBNLISTFILENAME);
			isbnList = (ArrayList<ISBNDTO>) isbnListObj;
			isbnList.add(isbnDTO);
		}

		int n = serial.objectToFileSave(isbnList, ISBNLISTFILENAME); // 저장한 데이터를 파일로 올려준다

		if (n == 1) {
			System.out.println(">>> 도서정보등록 성공!! <<<");
		} else {
			System.out.println(">>> 도서정보등록 실패!! <<<");
		}

	}

	// 5. 개별도서등록 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void bookRegist(Scanner sc) {

		String isbn = null;
		ISBNDTO isbndto = null; // 찾은 isbndto를 대입해주기 위해 미리 선언만 해둠

		System.out.println("\n== 개별도서 등록하기 ==");
		do { // 입력받은 데이터가 리스트와 겹치지 않을 시 다시 받아야 함

			System.out.print("> 국제표준도서번호(ISBN): ");
			isbn = sc.nextLine();

			if (isbn.trim().isEmpty()) { // isbn을 공백이나 엔터만 입력받은 경우(실패)
				System.out.println("~~~ ISBN을 입력하세요!!");
			} else if (isUseISBN(isbn)) { // isbn이 중복될 때 (통과)
				break;
			} else if (!isUseISBN(isbn)) { // isbn이 중복되지 않을 때 (실패)
				System.out.println(">>> 등록된 ISBN이 아닙니다. 도서등록 실패!! <<<\n");
			}

		} while (true);// do while 끝------------------------------

		// BookIdDto의 초기값으로 넣어줄 ISBNDTO를 찾아야 함
		Object isbnListObj = serial.getObjectFromFile(ISBNLISTFILENAME);
		List<ISBNDTO> isbnList = (ArrayList<ISBNDTO>) isbnListObj;

		for (int i = 0; i < isbnList.size(); i++) {
			if (isbn.equals(isbnList.get(i).getBookISBN())) {
				isbndto = isbnList.get(i);
			}
		}

		// Book Id 유효성 검사
		String id = null;
		List<BookIdDTO> idList = null;
		boolean flag = false; // 중복을 검사하기 위한 boolean
		do {
			flag = false; // 밑에서 이미 검사해서 중복일 수도 있으니 다시 false로 변경해줌
			System.out.print("> 도서아이디: ");
			id = sc.nextLine();

			if (id.trim().isEmpty()) {
				System.out.println("~~~ 도서 아이디를 입력하세요!!");
				continue;
			}

			Object idListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
			idList = (ArrayList<BookIdDTO>) idListObj;

			// 도서아이디가 이미 존재하는지 검사해야 함(널포인트 예외 처리) - 처음 파일이 존재하지 않을 시 예외가 뜸으로 예외처리를 해줌
			try {
				idList.isEmpty(); // 파일이 존재하지 않는지(리스트에 데이터가 아예 없는 상태인지) 검사
			} catch (Exception e) {
				break; // 파일이 아예 없는것으로 간주하여 아래 도서아이디를 받으러 넘어감
			}

			for (int i = 0; i < idList.size(); i++) {
				if (idList.get(i).getBookId().equals(id)) { // 리스트의 id값과 입력받은 id값이 겹칠 시
					System.out.println("~~~ " + id + " 는 이미 존재하므로 다른 도서아이디를 입력하세요!!");
					flag = true;
				}
			}

			// 아이디가 중복될 시 실행(밑으로 보내면 안됨)
			if (flag) {
				continue; // 아이디가 중복될 시 다시 아이디를 입력받게 위로 보내줌
			}
			break;
		} while (true); // do while 끝------------------------------

		List<BookIdDTO> bookIdList = null; // 파일 존재 유무때문에 null값으로 선언
		BookIdDTO bookIdDto = new BookIdDTO(id, isbn, false, isbndto); // 위에서 올바른 idbndto를 찾아서 초기값으로 대입해줌

		// bookList파일이 존재하는지를 확인
		File file = new File(BOOKIDLISTFILENAME);

		if (file.exists()) { // 파일이 존재하는 경우
			// 기존 파일 불러오기(bookIdList파일)
			// 기존 bookIdList ArrayList에 add해주기만 하면 됨
			Object bookIdListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
			bookIdList = (ArrayList<BookIdDTO>) bookIdListObj; // 위에 선언해둔 bookIdList에 형변환해서 넣어주기만 함
			bookIdList.add(bookIdDto);

		} else { // 파일이 존재하지 않는 경우
			// ArrayList를 new로 만들어 주고 add 해야함
			bookIdList = new ArrayList<>();
			bookIdList.add(bookIdDto);
		}

		// 저장한 데이터를 파일로 올려준다 save
		int n = serial.objectToFileSave(bookIdList, BOOKIDLISTFILENAME);

		if (n == 1) {
			System.out.println(">>> 도서등록 성공!! <<<");
		} else {
			System.out.println(">>> 도서등록 실패!! <<<");
		}

	}

	// 사서 아이디 유효성 검사
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseLibId(String id) {

		// 아이디 유효성 비교할 파일 불러오기
		Object obj = serial.getObjectFromFile(LIBLISTFILENAME);
		List<LibDTO> libdto = (ArrayList<LibDTO>) obj;

		// 처음 파일이 없을 때 그냥 뒤로 넘어가게 예외처리
		try {
			libdto.isEmpty(); // 파일이 비었을 때 걸림
		} catch (NullPointerException e) {
			return true; // 처음으로 파일이 생기는 때이니 중복될 아이디가 없으므로 true 반환
		}

		// 리스트를 돌려서 중복되는 아이디가 있는지 검색
		for (int i = 0; i < libdto.size(); i++) {
			// 아이디가 겹치면 다시 입력하게 함
			if (libdto.get(i).getLibId().equals(id)) {
				return false; // 아이디가 중복이니 false 반환
			}
		}

		return true; // 모든 아이디를 검사했는데 겹치는 값이 없으니 true 반환
	}

	// 1. 사서가입 메서드
	@SuppressWarnings("unchecked")
	public void libSignIN(Scanner sc) {

		System.out.println("\n== 사서 가입하기 ==");

		// 아이디 유효성 검사
		String libId = null;
		do {
			System.out.print("> 사서 ID: ");
			libId = sc.nextLine();

			if (libId.trim().isEmpty()) { // 공백이나 엔터가 들어왔을 시
				System.out.println("~~~ 사서 아이디를 입력하세요!!");
			} else if (isUseLibId(libId)) { // 중복된 아이디가 없을 시
				break;
			} else if (!isUseLibId(libId)) { // 중복된 아이디가 있을 시
				System.out.println("~~~ " + libId + " 는 이미 존재하므로 다른 사서 ID를 입력하세요!!");
			}

		} while (true); // do while문 끝 --------------------------------------------------

		System.out.print("> 암호: ");
		String libPwd = sc.nextLine();

		File file = new File(LIBLISTFILENAME); // 파일 존재 유무 검사를 위한 file클래스 호출

		List<LibDTO> libList = null; // 파일이 없을 시 쓰기 위해 선언만 해둠
		LibDTO libdto = new LibDTO(libId, libPwd); // 올바르게 받은 값을 libdto에 넣어줌

		if (file.exists()) { // 파일이 존재할 시
			Object libListObj = serial.getObjectFromFile(LIBLISTFILENAME);
			libList = (ArrayList<LibDTO>) libListObj;
			libList.add(libdto);

		} else { // 파일이 존재하지 않을 시
			libList = new ArrayList<>();
			libList.add(libdto);

		}

		// 파일을 저장시켜준다
		int n = serial.objectToFileSave(libList, LIBLISTFILENAME);

		// 저장상태 출력
		if (n == 1) {
			System.out.println(">>> 사서등록 성공!! <<<");
		} else {
			System.out.println(">>> 사서등록 실패!! <<<");
		}

	}

	// 2. 사서 로그인 메서드
	@SuppressWarnings("unchecked")
	@Override
	public LibDTO libLogin(Scanner sc) {

		System.out.println("\n== 로그인 하기 ==");

		System.out.print("> 사서 아이디: ");
		String libId = sc.nextLine();

		System.out.print("> 사서 암호: ");
		String libPwd = sc.nextLine();

		File file = new File(LIBLISTFILENAME);

		// 처음 파일자체가 없을 시 실행
		if (!file.exists()) {
			System.out.println(">>> 로그인 실패!!! <<<");
			return null;
		}

		// 파일로부터 모든 사서 로그인 정보 받아오기
		Object libListObj = serial.getObjectFromFile(LIBLISTFILENAME);
		List<LibDTO> libList = (ArrayList<LibDTO>) libListObj;

		LibDTO loginLib = new LibDTO(libId, libPwd); // 로그인 성공 시 반환할 dto객체

		// for문으로 모든 사원정보 확인
		for (int i = 0; i < libList.size(); i++) {
			// if문을 이용해 아이디 / 비번 대조 => 같은 값이 있을 시 로그인 성공
			if (libList.get(i).getLibId().equals(libId) && libList.get(i).getLibPwd().equals(libPwd)) { // 아이디 비번 대조
				System.out.println(">>> 로그인 성공!!! <<<");
				return loginLib; // 메인으로 로그인사서 반환 시키고 메서드 종료
			}
		}

		// 로그인 실패 출력
		System.out.println(">>> 로그인 실패!!! <<<");
		return null; // 로그인에 실패했을 때 메인으로 로그인사서에 null값을 보내줌
	}

	// 사서 메뉴 메서드
	@Override
	public void libMenu(Scanner sc) {

		LibDTO loginLib = null; // 현재 로그인 상태
		do {
			try {
				System.out.println("\n>>>> 사서 전용 메뉴 [ " + loginLib.getLibId() + " 로그인 중..] <<<<");
			} catch (Exception e) {
				System.out.println("\n>>>> 사서 전용 메뉴 <<<<");
			}

			String info = "1. 사서가입   2. 로그인   3. 로그아웃   4. 도서정보등록   5. 개별도서등록   6. 도서대여해주기   7. 대여중인도서조회   8. 도서반납해주기   9. 회원정보검색   10. 회원삭제하기  11. 나가기";

			System.out.println(info);
			System.out.print("메뉴번호선택: ");
			String menu = sc.nextLine();

			switch (menu) {
			case "1":
				libSignIN(sc); // 1. 사서가입
				break;

			case "2":
				// 2. 로그인
				if (loginLib == null) { // 로그아웃 상태
					loginLib = libLogin(sc);
				} else { // 로그인 상태
					System.out.println("~~~ 이미 로그인이 되어있는 상태입니다!!!");
				}

				// 처음 로그인 전 상태일때를 if문으로 구분해줘야 함 <= 쓸지 안쓸지 모름
				break;

			case "3":
				// 3. 로그아웃
				if (loginLib != null) { // 로그인 되어있는 상태
					loginLib = null;
					System.out.println(">>> 로그아웃 완료!!! <<<");
				} else {
					System.out.println("~~~ 이미 로그아웃이 되어있는 상태입니다!!!");
				}
				break;

			case "4":
				if (loginLib != null) {
					ISBNRegist(sc); // 4. 도서정보등록
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}

				break;

			case "5":
				if (loginLib != null) {
					bookRegist(sc); // 5. 개별도서등록
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "6":
				if (loginLib != null) {
					bookRental(sc); // 6. 도서대여해주기
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "7":
				if (loginLib != null) {
					rentedBook(); // 7. 대여중인도서조회
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "8":
				if (loginLib != null) {
					bookReturn(sc); // 8. 도서반납해주기
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "9":
				if (loginLib != null) {
					showUser(sc); // 9. 검색한 회원정보 보기
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "10":
				if (loginLib != null) {
					deleteUser(sc); // 10. 회원 삭제
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "11":
				System.out.println();
				return; // 11. 나가기

			default:
				System.out.println("잘못된 번호를 입력하셨습니다!");
				break;
			}
		} while (true);

	}

	// 9. 회원삭제 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void deleteUser(Scanner sc) {

		System.out.println("\n>>> 회원삭제하기 <<<");

		String userId = null;
		// 입력받은 값이 올바른지 확인
		do { // start of do-while--------------------------------------
			System.out.print("삭제할 회원의 ID: ");
			userId = sc.nextLine();

			if (userId.trim().isEmpty()) { // 공백이나 엔터만 입력했을 시(실패)
				System.out.println("~~~ 올바른 값을 입력해주세요!!");
				continue;
			} else if (isUseUserId(userId)) { // 입력받은 값이 회원에 존재하지 않는 ID인지 (실패) 중복시 false
				System.out.println("~~~ 존재하지 않는 회원입니다!!");
				continue; // 다시 회원ID를 입력받게 함
			} else if (!isUseUserId(userId)) { // 입력받은 값이 존재하는 회원일 시(통과)
				break; // 입력받은 회원ID가 DB에 존재 할시 do-while문을 빠져나감
			}
		} while (true);// end of do-while--------------------------------------

		// RentalTask를 파일에서 불러옴
		Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
		List<RentalTask> rentalList = (ArrayList<RentalTask>) rentalListObj;

		// 입력받은 회원의 객체를 찾아내서 도서를 대여중인지 확인
		boolean flag = false; // true-대여중 / false - 대여중이 아님
		for (int i = 0; i < rentalList.size(); i++) {
			if (rentalList.get(i).getUserId().equals(userId)) { // 만약 입력받은 회원ID가 DB에 존재한다면
				// 책을 빌린 상태이니 탈퇴를 시킬 수 없음
				flag = true;
			}
		}

		if (flag) { // 도서를 대여중인 상태(실패)
			System.out.println("~~~ 해당 회원은 도서를 대여중이므로 삭제를 할 수 없습니다!!");
			return; // 만약 대여중인 회원이라면 메서드를 끝내고 나감
		}

		// 도서를 대여중이지 않은 상태(성공) - 회원삭제 가능!

		// 사용자에게 한번 더 삭제할 것인지 물어봄
		do {
			System.out.print("정말 " + userId + " 님의 회원정보를 삭제 하시겠습니까?(Y/N):  ");
			String choice = sc.nextLine();

			if (choice.equalsIgnoreCase("y")) { // 삭제를 선택
				break;
			} else { // 삭제하지 않는 것을 선택
				System.out.println(">>> 회원정보 삭제를 취소하였습니다. <<<");
				return;
			}
		} while (true);

		// 유저정보 파일로부터 불러오기
		Object userListObj = serial.getObjectFromFile(USERLISTFILENAME);
		List<UserDTO> userList = (ArrayList<UserDTO>) userListObj;

		// list에서 입력받은 회원ID로 검색해서 해당 dto 찾아내기
		for (int i = 0; i < userList.size(); i++) {
			if (userId.equals(userList.get(i).getUserId())) { // 입력받은 ID와 같은 회원을 찾았을 시
				userList.remove(i); // 해당 회원 dto 삭제
			}
		}

		// 업데이트된 정보를 파일에 올려줌
		int n = serial.objectToFileSave(userList, USERLISTFILENAME);

		// 저장상태 출력
		if (n == 1) {
			System.out.println(">>> 회원삭제 성공!! <<<");
		} else {
			System.out.println(">>> 회원삭제 실패!! <<<");
		}

	}

	// 6. 도서대여해주기 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void bookRental(Scanner sc) {

		System.out.println("\n>>> 도서대여하기 <<<");

		// 회원 id 유효성 검사
		String userId = null;
		do {

			System.out.print("> 회원ID: ");
			userId = sc.nextLine();

			if (userId.trim().isEmpty()) { // 공백이나 엔터를 입력받았을 시
				System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~\n");
			} else if (!isUseUserId(userId)) { // 중복된 값이 있을 시 (while문을 빠져나가야 함)
				break;
			} else if (isUseUserId(userId)) { // 중복된 값이 없었을 시
				System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~\n");
				continue;
			}

		} while (true);

		String num = null;
		do {
			System.out.print("> 총 대여권수: ");
			num = sc.nextLine();

			if (num.trim().isEmpty()) {
				System.out.println("~~~ 대여권수를 입력해주세요!!");
				continue;
			}
			try {
				Integer.parseInt(num); // 문자값을 입력받았을 시 다시 입력받게 함
			} catch (NumberFormatException e) {
				System.out.println("~~~ 대여권수는 숫자로 입력해주세요!!");
				continue;
			}

			if (Integer.parseInt(num) < 1) {
				System.out.println("~~~ 올바른 대여권수 입력해주세요!!");
				continue;
			}

			break;
		} while (true);

		// 도서ID 유효성 검사
		// 잘못된 값을 입력해서 다시 입력해야 할 시 총대여권수에 맞게 횟수를 늘려줘야 함(for을 사용하여 i로 조정)
		List<RentalTask> rentalList = null; // 불러올 파일을 저장할 리스트
		for (int i = 0; i < Integer.parseInt(num); i++) { // num을 String형으로 받아서 잠시 int로 변환
			System.out.print("> 도서ID: ");
			String bookId = sc.nextLine();

			// 도서ID가 겹쳐야 뒤로 넘어 갈 수 있음
			if (bookId.trim().isEmpty()) { // 공백이나 엔터가 들어왔을 시 (실패)
				System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~");
				i--; // 잘못된 값을 입력했을 시 반복횟수를 줄여서 다시 입력받게 함
				continue;
			} else if (!isUseBookId(bookId)) { // 도서 아이디가 겹치지 않을 시 (실패), -> 도서가 이미 대여중일 시(해당 출력은 유효성 검사 메서드에서 함)
				i--;
				continue;
			} else if (isUseBookId(bookId)) { // 도서 아이디가 겹칠 시 (통과)
				// for문으로 반복 횟수는 정해져있고, 잘못 입력받았을 시 다시 입력받아야 하므로 break는 쓰지 않음

			}

			// 도서 아이디를 입력 받았을 시 파일의 정보(대여/비치)를 변경해줘야 함

			// 파일 자체가 있는지 없는지를 확인
			File file = new File(RENTALLISTFILENAME);

			// 입력받은 회원 ID와 겹치는 dto객체를 받아옴
			Object userListObj = serial.getObjectFromFile(USERLISTFILENAME);
			List<UserDTO> userList = (ArrayList<UserDTO>) userListObj;
			UserDTO userdto = null; // 겹치는 값을 넣어둘 dto

			for (int j = 0; j < userList.size(); j++) {
				if (userId.equals(userList.get(j).getUserId())) { // 검색한 회원ID와 겹칠 시
					userdto = userList.get(j); // 해당 회원의 정보를 userdto에 넣음
				}
			}

			// 입력한 도서ID의 ISBN객체를 받아옴
			Object bookIdListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
			List<BookIdDTO> bookIdList = (ArrayList<BookIdDTO>) bookIdListObj;
			ISBNDTO isbnDto = null;

			for (int j = 0; j < bookIdList.size(); j++) {
				if (bookId.equals(bookIdList.get(j).getBookId())) {
					isbnDto = bookIdList.get(j).getIsbndto();
				}
			}

			RentalTask rentalTask = new RentalTask();
			rentalTask.setUserId(userId); // 대여 dto에 회원 아이디 넣어줌
			rentalTask.setBookId(bookId); // 대여 dto에 도서ID 넣어줌
			rentalTask.setRentalDay(); // 대여 dto에 대여일 넣어줌
			rentalTask.setReturnDay(); // 대여 dto에 반납일 넣어줌
			rentalTask.setRentalAvailable(bookId, true); // 입력받은 bookId를 찾아 대여중으로 바꿔주었음
			rentalTask.setUserDto(userdto); // 대여하는 회원의 정보 객체를 필드에 저장
			rentalTask.setIsbnDto(isbnDto); // 빌리는 책의 ISBN 정보 객체를 필드에 저장

			if (file.exists()) { // 파일이 존재한다면 파일을 불러온 뒤 데이터 저장 작업을 해줌
				Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
				rentalList = (ArrayList<RentalTask>) rentalListObj;

				rentalList.add(rentalTask);

			} else { // 파일이 존재하지 않는다면 객체를 새로 만들어준 후 데이터 작업
				rentalList = new ArrayList<>();
				rentalList.add(rentalTask);
			}

			// 변경한 정보들을 파일에 저장시킴
			serial.objectToFileSave(rentalList, RENTALLISTFILENAME);

		} // end of for ----------------------------------------------------------

		// 대여에 성공할 시 초기값을 줄 때 회원ID와 도서ID로 해당 DTO를 검색하여 RentalTask객체 필드에 넣어줘야 함

		System.out.println(">>> 대여등록 성공!! <<<");
		System.out.println(">>> 대여도서 비치중에서 대여중으로 변경함 <<<");

	}

	// 7. 대여중인도서조회
	@SuppressWarnings("unchecked")
	@Override
	public void rentedBook() {

		String info = "도서ID\t\t\tISBN\t\t\t도서명\t\t작가명\t출판사\t회원ID\t연락처\t\t대여일자";

		System.out.println(
				"===========================================================================================================================");
		System.out.println(info);
		System.out.println(
				"===========================================================================================================================");

		File file = new File(RENTALLISTFILENAME);

		// 처음 파일자체가 없을 시 실행
		if (!file.exists()) {
			System.out.println("~~~ 대여중인 도서가 존재하지 않습니다. ~~~");
			return;
		}

		Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
		List<RentalTask> rentalList = (ArrayList<RentalTask>) rentalListObj;

		for (RentalTask list : rentalList) {
			System.out.println(list.getBookId() + "\t\t\t" + list.getIsbnDto().getBookISBN() + "\t\t\t"
					+ list.getIsbnDto().getBookName() + "\t\t" + list.getIsbnDto().getBookAuthorName() + "\t"
					+ list.getIsbnDto().getBookPublisher() + "\t" + list.getUserId() + "\t"
					+ list.getUserDto().getUserPhone() + "\t" + list.getRentalDay());

		}

	}

	// 8. 도서반납해주기 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void bookReturn(Scanner sc) {

		System.out.println("\n>>> 도서반납하기 <<<");
		List<RentalTask> rentalList = null;
		List<BookIdDTO> bookIdList = null;
		// 총반납권수 입력받기
		String num = null;
		int nNum = 0;
		do { // start of do-while---------------------------------------------

			System.out.print("> 총반납권수: ");
			num = sc.nextLine();

			if (num.trim().isEmpty()) { // 공백이나 엔터만 입력할 시 출력
				System.out.println("~~~ 반납권수를 입력해주세요!!");
				continue;
			}

			// 문자를 입력할 시 예외 처리
			try {
				nNum = Integer.parseInt(num);

			} catch (Exception e) {
				System.out.println("~~~ 숫자만 입력해주세요!!");
				continue;
			}

			if (nNum < 1) {
				System.out.println("~~~ 올바른 대여권수 입력해주세요!!");
				continue;
			}

			break;
		} while (true);// end of do-while------------------------------------------

		// 반납도서 유무 검사
		String bookId = null;
		int sumArrears = 0;

		for (int i = 0; i < nNum; i++) { // start of for----------------------------------

			do {
				System.out.print("반납도서ID: ");
				bookId = sc.nextLine();

				if (bookId.trim().isEmpty()) { // 공백이나 엔터만 받는다면 (실패)
					System.out.println("~~~ 도서ID를 입력해주세요!!");
				}

				boolean flag = false;
				// rental에서 검색하기
				Object obj = serial.getObjectFromFile(RENTALLISTFILENAME);
				List<RentalTask> rList = (ArrayList<RentalTask>) obj;
				for (RentalTask list : rList) {
					// 해당하는 값이 있다면 (통과)
					if (bookId.equals(list.getBookId())) {
						flag = true;
						break;
					}
					// 없다면 실패
				}

				if (flag) {
					break;
				} else {
					System.out.println("~~~ 잘못된 값을 입력하셨습니다!!");
				}
			} while (true);

			// RentalTask(모든 대여 도서 데이터)에서 입력받은 도서ID가 있나 검색
			// RentalTask를 파일로부터 불러오기
			Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
			rentalList = (ArrayList<RentalTask>) rentalListObj;

			Object bookIdListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
			bookIdList = (ArrayList<BookIdDTO>) bookIdListObj;

			boolean flag = false;
			// 입력받은 도서ID를 DB내에서 검색

			for (int j = 0; j < rentalList.size(); j++) {
				if (bookId.equals(rentalList.get(j).getBookId())) { // 대여 도서 목록에 있는 도서ID와 겹친다면 해당 (통과)
					System.out.println("도서별 연체료: " + rentalList.get(j).payArrears() + "원");
					sumArrears += rentalList.get(j).payArrears(); // 연체료
					rentalList.remove(j); // 여기서 해당 rentalList에 dto를 삭제시켜줘야함

					for (BookIdDTO book : bookIdList) {
						if (bookId.equals(book.getBookId())) { // id가 같다면
							book.setRent(false); // 비치중으로 변경
						}
					}
					flag = true;
					break; // 겹치는 도서ID를 찾았다면 for문을 빠져나가 그 다음 도서ID를 입력받음

				}

			}
			// 대여 도서 목록에 있는 도서ID와 겹치지 않는다면 (한번더 j--)
			if (!flag) {
				System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~\n"); // 여기, 조심 continue문 필요할지도?
				i--;
				continue;
			}

			// 제대로 된 도서ID값을 받았을 시에만 넘어올 수 있음

			// for문 한번마다 방금 업데이트된 정보를 파일에 저장시켜줌
			serial.objectToFileSave(rentalList, RENTALLISTFILENAME);
			serial.objectToFileSave(bookIdList, BOOKIDLISTFILENAME);

		} // end of for-----------------------------------------------------------------

		System.out.println("> 연체료 총계: " + sumArrears + "원");
		// 파일로 업데이트 시키기(여기, 꼭 필요할지 위에 있는데)
		int n = serial.objectToFileSave(rentalList, RENTALLISTFILENAME);
		serial.objectToFileSave(bookIdList, BOOKIDLISTFILENAME);

		if (n == 1) {
			System.out.println(">>> 도서반납 성공!! <<<");
			System.out.println(">>> 대여도서 대여중에서 비치중으로 변경함 <<<");
		} else {
			System.out.println(">>> 도서등록 실패!! <<<");
		}

	}

	// 1. 일반회원가입
	@SuppressWarnings("unchecked")
	@Override
	public void UserSignIn(Scanner sc) {

		System.out.println("\n== 일반회원 가입하기 ==");

		// Id 유효성 검사
		String userId = null;
		do {

			System.out.print("> 회원 ID: ");
			userId = sc.nextLine();

			// 공백-엔터 검사
			if (userId.trim().isEmpty()) {
				System.out.println("~~~ 회원아이디를 입력하세요!!");
				continue;
			} else if (isUseUserId(userId)) { // 아이디가 중복되지 않았을 시
				break;
			} else if (!isUseUserId(userId)) { // 아이디가 중복됐을 시
				System.out.println("~~~ " + userId + " 는 이미 존재하므로 다른 회원ID를 입력하세요!!");
			}

		} while (true); // 유효성 검사 do-while 끝 -----------------------------------------

		System.out.print("> 암호: ");
		String userPwd = sc.nextLine();

		System.out.print("> 성명: ");
		String userName = sc.nextLine();

		System.out.print("> 연락처: ");
		String userPhone = sc.nextLine();

		// 입력받은 값을 UserDTO에 저장
		// 주의 - bookiDto에 null값으로 들어가잇음 -> 회원가입 당시에는 책을대여했을리가 없기 때문?
		UserDTO userdto = new UserDTO(userId, userPwd, userName, userPhone, null);

		// 파일로 보내줄 ArrayList 선언
		List<UserDTO> userList = null;

		// 현재 첫가입인지 userList파일이 존재하는지 검사
		File file = new File(USERLISTFILENAME);

		if (file.exists()) { // 파일이 존재할 때
			// 기존의 파일 불러오기
			// 불러온 파일에 dto add
			Object obj = serial.getObjectFromFile(USERLISTFILENAME);
			userList = (ArrayList<UserDTO>) obj;
			userList.add(userdto);

		} else { // 파일이 존재하지 않을 때
			// arraylist를 새로 객체 생성
			userList = new ArrayList<>();
			// 해당 리스트에 dto add
			userList.add(userdto);
		}

		// arraylist를 파일에 저장시켜주기
		int n = serial.objectToFileSave(userList, USERLISTFILENAME);

		// 저장여부로 성공/실패 출력
		if (n == 1) {
			System.out.println(">>> 회원등록 성공!! <<<");
		} else {
			System.out.println(">>> 회원등록 실패!! <<<");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDTO UserLogin(Scanner sc) {

		System.out.println("\n== 로그인 하기 == ");

		System.out.print("> 회원아이디: ");
		String userId = sc.nextLine();

		System.out.print("> 암호: ");
		String userPwd = sc.nextLine();

		File file = new File(USERLISTFILENAME);

		if (!file.exists()) { // 파일이 존재하지 않는다면
			System.out.println(">>> 로그인 실패!! <<<");
			return null;
		}

		// 아이디 비번을 대조할 사용자 파일을 불러옴
		Object userListObj = serial.getObjectFromFile(USERLISTFILENAME);
		List<UserDTO> userList = (ArrayList<UserDTO>) userListObj;

		// 로그인이 성공한 값을 저장할 객체 선언
		UserDTO userdto = null;

		// for문과 if문으로 아이디 비번 대조
		for (int i = 0; i < userList.size(); i++) {
			// 만약 아이디 비번이 같을 시 로그인 성공 => userdto에 리스트에서 찾은 객체를 저장, 반환시킴
			if (userList.get(i).getUserId().equals(userId) && userList.get(i).getUserPwd().equals(userPwd)) {
				userdto = userList.get(i); // 로그인 성공한 dto를 리스트에서 뽑아서 userdto에 넣어줌
				System.out.println(">>> 로그인 성공!! <<<");
				return userdto; // 로그인 성공시 객체를 반환
			}
		}

		// 로그인 실패
		System.out.println(">>> 로그인 실패!! <<<");

		return null; // 로그인 실패시 null값을 반환
	}

	@Override
	public void UserMenu(Scanner sc) {

		UserDTO loginUser = null; // 현재 로그인 상태
		do {
			try {
				System.out.println("\n>>>> 일반회원 전용 메뉴 [ " + loginUser.getUserName() + " 로그인 중..] <<<<");
			} catch (Exception e) {
				System.out.println("\n>>>> 일반회원 전용 메뉴 <<<<");
			}

			String info = "1. 일반회원가입   2. 로그인   3. 로그아웃   4. 도서검색하기   5. 나의대여현황보기   6. 나가기";

			System.out.println(info);
			System.out.print("메뉴번호선택: ");
			String menu = sc.nextLine();

			switch (menu) {
			case "1":
				UserSignIn(sc); // 1. 일반회원가입
				break;

			case "2":
				// 2. 로그인
				if (loginUser == null) { // 로그아웃 상태
					loginUser = UserLogin(sc);
				} else { // 로그인 상태
					System.out.println("~~~ 이미 로그인이 되어있는 상태입니다!!!");
				}

				// 처음 로그인 전 상태일때를 if문으로 구분해줘야 함 <= 쓸지 안쓸지 모름
				break;

			case "3":
				// 3. 로그아웃
				if (loginUser != null) { // 로그인 되어있는 상태
					loginUser = null;
					System.out.println(">>> 로그아웃 완료!!! <<<");
				} else {
					System.out.println("~~~ 이미 로그아웃이 되어있는 상태입니다!!!");
				}
				break;

			case "4":
				if (loginUser != null) {
					BookSearch(sc); // 4. 도서검색하기
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}

				break;

			case "5":
				if (loginUser != null) {
					showMyRent(loginUser); // 5. 나의대여현황보기
				} else {
					System.out.println("~~~~ 로그인 상태에서만 이용 가능한 메뉴입니다!!!");
				}
				break;

			case "6":
				System.out.println();
				return; // 6. 나가기

			default:
				System.out.println("잘못된 번호를 입력하셨습니다!");
				break;
			}
		} while (true);

	}

	// 4. 도서검색하기 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void BookSearch(Scanner sc) {

		System.out.println("\n>>> 도서검색하기 <<<");
		System.out.println("[주의사항] 검색어를 입력하지 않고 엔터를 하면 검색대상에서 제외됩니다.");

		System.out.print("> 도서분류카테고리(Programming, Database 등): ");
		String category = sc.nextLine();
		if (category.trim().isEmpty()) { // 공백이나 엔터를 받았을 시
			category = ""; // 카테고리를 무조건 참으로 선언 => ""은 boolean에서 무조건 참이라는 뜻?
		}

		System.out.print("> 도서명: ");
		String bookName = sc.nextLine();
		if (bookName.trim().isEmpty()) {
			bookName = "";
		}

		System.out.print("> 작가명: ");
		String author = sc.nextLine();
		if (author.trim().isEmpty()) {
			author = "";
		}

		System.out.print("> 출판사명: ");
		String company = sc.nextLine();
		if (company.trim().isEmpty()) {
			company = "";
		}

		// 모든 BookIdDto 정보를 파일에서 불러옴
		Object bookIdListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
		List<BookIdDTO> bookIdList = (ArrayList<BookIdDTO>) bookIdListObj;

		String info = "ISBN\t\t\t도서아이디\t\t\t도서명\t\t\t작가명\t출판사\t가격\t대여상태";

		System.out.println(
				"======================================================================================================");
		System.out.println(info);
		System.out.println(
				"======================================================================================================");

		File file = new File(BOOKIDLISTFILENAME);

		// 처음 파일자체가 없을 시 실행
		if (!file.exists()) {
			System.out.println("~~~ 도서가 존재하지 않습니다. ~~~");
			return;
		}

		boolean flag = false; // 검색결과 유무를 검사해줄 변수
		for (BookIdDTO list : bookIdList) {

			// System.out.println(list.getIsbndto().getBookCatagory());
			// toLowerCase() - 문자를 소문자로 변환(입력받은 값을 대소문자 구분없이 처리하기 위해 사용)
			// startsWith() - 문자의 첫부분이 입력받은 값과 같으면 true, 같지 않다면 false 반환
			boolean b_category = list.getIsbndto().getBookCatagory().toLowerCase().startsWith(category);
			boolean b_bookName = list.getIsbndto().getBookName().toLowerCase().startsWith(bookName);
			boolean b_author = list.getIsbndto().getBookAuthorName().toLowerCase().startsWith(author);
			boolean b_company = list.getIsbndto().getBookPublisher().toLowerCase().startsWith(company);

			// 받은 값이 모두 비어있을 시(enter 또는 공백) 전체 정보 출력
			if (b_category && b_bookName && b_author && b_company) { // 입력받은 값과 dto내의 값을 비교해서 boolean으로 각 값이 같은지를 확인,
				// 하나라도 같지 않다면 출력 x
				flag = true;
				System.out.println(list); // BookIdDto에서 오버라이딩한 toString() 메서드로 메세지를 출력해줌
			}
		}

		if (!flag) { // 검색조건에 부합하는 결과값이 없을 시
			System.out.println("~~~~ 검색에 일치하는 도서가 없습니다. ~~~~");
		}

	}

	// 5. 나의대여현황보기 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void showMyRent(UserDTO user) {

		String info = "도서ID\t\t\tISBN\t\t\t도서명\t\t작가명\t출판사\t회원ID\t\t대여일자\t\t반납예정일";

		System.out.println(
				"=================================================================================================================================");
		System.out.println(info);
		System.out.println(
				"=================================================================================================================================");

		// 대여된 책 목록 전체를 파일에서 받아옴
		Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
		List<RentalTask> rentalList = (ArrayList<RentalTask>) rentalListObj;

		boolean flag = false; // 해당 아이디로 대여된 도서가 없을 때 출력문을 띄우기 위한 boolean

		File file = new File(RENTALLISTFILENAME);

		if (!file.exists()) {
			System.out.println("~~~ 대여해가신 도서가 없습니다. ~~~");
			return;
		}

		for (RentalTask list : rentalList) {
			if (user.getUserId().equals(list.getUserId())) {
				try {
					System.out.println(list.getBookId() + "\t\t\t" + list.getIsbnDto().getBookISBN() + "\t\t\t"
							+ list.getIsbnDto().getBookName() + "\t\t" + list.getIsbnDto().getBookAuthorName() + "\t"
							+ list.getIsbnDto().getBookPublisher() + "\t" + list.getUserId() + "\t\t"
							+ list.getRentalDay() + "\t" + list.getReturnDay());
					flag = true; // 대여된 도서가 있으면 true
				} catch (NullPointerException e) {
					System.out.println("~~~ 대여해가신 도서가 없습니다. ~~~");
				}
			}
		}
		if (!flag) { // 해당 아이디로 대여된 도서가 없을 때 출력
			System.out.println("~~~ 대여해가신 도서가 없습니다. ~~~");
		}

	}

	// 유저 아이디 유효성 검사 메서드
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseUserId(String id) {

		// 아이디 비교를 위한 사용자 정보 불러오기
		Object obj = serial.getObjectFromFile(USERLISTFILENAME);
		List<UserDTO> userList = (ArrayList<UserDTO>) obj;

		// 파일이 없다는 가정하에 코드
		// 없으면 null 예외처리

		File file = new File(USERLISTFILENAME);

		if (!file.exists()) { // 파일이 존재하지 않을 시
			return true;
		}

		// 파일 있다는 가정하에 검사
		for (int i = 0; i < userList.size(); i++) { // 아이디 중복이 존재하면 다시 입력받게 구현
			if (userList.get(i).getUserId().equals(id)) { // id가 중복이라면
				return false; // 중복된 아이디가 있으므로 false 반환
			}
		}

		return true; // 모든 아이디를 검사했는데 중복된 값이 없으므로 true 반환
	}

	// 책 고유 아이디 유효성 검사
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseBookId(String id) {

		// 파일에서 전체 도서ID 목록을 불러옴
		Object bookIdListObj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
		List<BookIdDTO> bookIdList = (ArrayList<BookIdDTO>) bookIdListObj;

		for (BookIdDTO list : bookIdList) { // 여기, 두번씩 실행되는 경우가 잇음 밑에 syso로 전체값을 출력할 시
			if (list.getBookId().equals(id)) { // 검색한 아이디와 db에 중복된 값이 있을 시

				if (list.isRent()) { // 검색된 도서ID가 이미 대여중인 상태일 때
					System.out.println("~~~ 해당 도서는 이미 대여중입니다!!");
					return false; // 이미 대여중이니 대여 할 수 없음, false 반환 (여기, 나중에 대여/비치 설정하면 다시 와서 확인하기)
				}
				return true; // 도서ID가 겹치면 true 반환
			}
		}

		System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~");
		return false; // 도서ID가 겹치지 않을 시 false 반환
	}

	// 검색한 회원 정보 보기 메서드(회원 정보 아래에 대여한 책 정보)
	@SuppressWarnings("unchecked")
	@Override
	public void showUser(Scanner sc) {

		// 회원ID 입력받기, 유효성 검사
		String userId = null;
		do {
			System.out.print("\n>>> 회원정보 검색하기 <<<\n\n");
			System.out.print("검색할 회원ID: ");
			userId = sc.nextLine();

			if (userId.trim().isEmpty()) { // 엔터나 공백만 받았을 시 (실패)
				System.out.println("~~~ 회원ID를 입력해주세요!!");
				continue;
			} else if (isUseUserId(userId)) { // 입력한 회원ID가 DB에 없을 시 (실패)
				System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~");
				continue;
			} else { // 입력한 회원ID가 DB에 있을 시 (성공)
				break; // 회원ID찾기 성공, do-while문 탈출
			}
		} while (true);

		// UserDTO 받아오기(회원정보 출력용)
		Object userListObj = serial.getObjectFromFile(USERLISTFILENAME);
		List<UserDTO> userList = (ArrayList<UserDTO>) userListObj;

		UserDTO userDto = null; // 검색한 회원의 정보를 담을 dto

		// 회원ID로 중복값 검색
		for (int i = 0; i < userList.size(); i++) {
			if (userId.equals(userList.get(i).getUserId())) { // 입력받은 값과 ID가 같을 시
				userDto = userList.get(i); // 검색한 회원의 정보를 userDto에 담음
				break; // 회원을 찾았으니 for문 탈출
			}
		}

		// 회원정보 출력

		System.out.println("\n>>> 내정보 <<<\n");

		System.out.println("> 성명: " + userDto.getUserName());
		System.out.println("> 회원ID: " + userDto.getUserId());
		System.out.println("> 암호: " + userDto.getUserPwd());
		System.out.println("> 연락처: " + userDto.getUserPhone());

		// Rentaltask 받아오기 (해당 ID의 대여정보 출력 용)
		Object rentalListObj = serial.getObjectFromFile(RENTALLISTFILENAME);
		List<RentalTask> rentalList = (ArrayList<RentalTask>) rentalListObj;

		try {
			rentalList.isEmpty();
		} catch (NullPointerException e) {
			System.out.println("\n~~~ 대여한 도서 없습니다!!");
			return;
		}

		// 검색한 회원의 대여 정보를 담을 ArrayList 생성
		List<RentalTask> rentalListChoice = new ArrayList<>();
		// 회원ID로 RentalTask에서 찾음
		for (int i = 0; i < rentalList.size(); i++) {
			if (userId.equals(rentalList.get(i).getUserId())) { // 입력받은 값과 ID가 같을 시
				rentalListChoice.add(rentalList.get(i)); // 해당 회원의 대여 정보를 List에 담음
			}
		}

		if (rentalList.isEmpty()) { // 만약 회원이 대여한 도서가 없을 시 메서드를 끝냄
			return;
		}

		// 대여정보 출력
		String info1 = "도서ID\t\t\t도서명\t\t작가명\t출판사\t대여일자\t\t반납예정일";
		System.out.println(
				"===================================================================================================");
		System.out.println(info1);
		System.out.println(
				"===================================================================================================");
		for (RentalTask list : rentalListChoice) {

			System.out.println(list.getBookId() + "\t\t\t" + list.getIsbnDto().getBookName() + "\t\t"
					+ list.getIsbnDto().getBookAuthorName() + "\t" + list.getIsbnDto().getBookPublisher() + "\t"
					+ list.getRentalDay() + "\t" + list.getReturnDay());
		}

	}

}
