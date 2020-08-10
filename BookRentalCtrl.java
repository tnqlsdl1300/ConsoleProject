package project.bookrental.management;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookRentalCtrl implements InterBookRentalCtrl {
	
	private final String LIBRARIAN_FILE = "C:/iotestdata/project/bookrental/librarian.dat"; 
	private final String BOOKINFOLIST_FILE = "C:/iotestdata/project/bookrental/bookinfolist.dat";
	private final String RENTALBOOKLIST_FILE = "C:/iotestdata/project/bookrental/rentalbooklist.dat";
	private final String RENTALUSERLIST_FILE = "C:/iotestdata/project/bookrental/rentaluserlist.dat";
	private final String RENTALTASKLIST_FILE = "C:/iotestdata/project/bookrental/rentaltasklist.dat";
	
	private BookRentalSerializable serial = new BookRentalSerializable();
	
	// ~~~~~~~~~~ 1. 사서 전용 메뉴를 만든다. ~~~~~~~~~~
	// == 사서 전용메뉴 == 
	@Override
	public void librarianMenu(Scanner sc) {
		
		Librarian lib = null;
		
		String sMenuno = "";
		
		do {
			String loginId = (lib==null)?"":"["+lib.getLbid()+" 로그인중..]";
			
			String sMenu = "\n>>>> 사서 전용 메뉴 "+loginId+"<<<<\n" + 
				      "1.사서가입    2.로그인    3.로그아웃   4.도서정보등록   5.개별도서등록\n"+
					  "6.도서대여해주기    7.대여중인도서조회    8.도서반납해주기    9.나가기\n" + 
	                  "=> 메뉴번호선택 : ";
		
			System.out.print(sMenu);
		
			sMenuno = sc.nextLine(); // 사서전용 Menu 메뉴번호 선택하기
		
			switch (sMenuno) {
				case "1":  // 사서가입
					registerLibrarian(sc);
					break;
					
				case "2":  // 로그인
					lib = loginLib(sc);
					break;
					
				case "3":  // 로그아웃
					lib = null;
					break;
					
				case "4":  // 도서정보등록
					if(lib != null) {
						registerBookInformation(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;
					
				case "5":  // 개별도서등록
					if(lib != null) {
						registerRentalBook(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;	
					
				case "6":  // 도서대여해주기
					if(lib != null) {
						rental(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;
					
				case "7":  // 대여중인 모든 도서조회
					if(lib != null) {
						rentAllView();
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;
					
				case "8":  // 도서반납해주기
					if(lib != null) {
						bookReturn(sc);  
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;	
					
				case "9":  // 나가기
					break;
				
				default:
					System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");
					break;
			}	
			
		} while (!"9".equals(sMenuno));	
		
	}// end of public void librarianMenu(Scanner sc)----------------------------------------
	
	
	
	// ~~~~~~~~~~ 3. 사서객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 3.1 사서가입(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerLibrarian(Scanner sc) {
		
		System.out.println("\n== 사서가입하기 ==");
		String libid = "";
		
		// 사서ID 중복검사하기 
		do {
			System.out.print("▶ 사서ID: ");
			libid = sc.nextLine();  // hongkd , seoyh
			
			if(libid==null || libid.trim().isEmpty()) {
				System.out.println("~~~ 사서아이디를 입력하세요!!");
				continue;
			}
			else if(!isUseLibID(libid)) {
				System.out.println("~~~ "+libid+" 는 이미 존재하므로 다른 사서ID를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
				
		System.out.print("▶ 암호: ");  // 1234 , 1234
		String pwd = sc.nextLine();
				
		Librarian librarian = new Librarian(libid, pwd);
				
		File file = new File(LIBRARIAN_FILE); // LIBRARIAN_FILE 에 해당하는 파일객체 생성하기
		Map<String, Librarian> librarianMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			librarianMap = new HashMap<>();
			librarianMap.put(libid, librarian);
			n = serial.objectToFileSave(librarianMap, LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 librarianMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입시
			Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 저장된 객체를 불러온다.
			librarianMap = (HashMap<String, Librarian>) librarianMapObj;
			librarianMap.put(libid, librarian);
			n = serial.objectToFileSave(librarianMap, LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 librarianMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 사서등록 성공!! <<<");
		else
			System.out.println(">>> 사서등록 실패!! <<<");
		
	}// end of public void registerLibrarian(Scanner sc)-----------------------------------------

	
	// == 3.2 사서 아이디 중복 검사하기 ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseLibID(String libid) {
		
		boolean isUse = true; // 최초로 가입시 파일에 저장된 librarianMapObj 객체가 없기애 중복된 회원아이디가 없으므로 사용가능하도록 한다.
		
		Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); 
		
		if(librarianMapObj != null) { // 파일에 저장된 librarianMapObj 객체가 있다라면 
			HashMap<String, Librarian> librarianMap = (HashMap<String, Librarian>)librarianMapObj;
			Librarian lib = librarianMap.get(libid);
			if(lib != null) { // 사서아이디가 존재하는 경우이라면 
				isUse = false;
			}
		}
		 
		return isUse;
	}// end of public boolean isUseLibID(String libid)-----------------------------------	

	
	// ~~~~~~~~~~ 4. 사서로 로그인 하기 ~~~~~~~~~~ 
	@SuppressWarnings("unchecked")
	@Override
	public Librarian loginLib(Scanner sc) { // 사서로 로그인 하기
	
		Librarian lib = null;
		
		System.out.println("\n == 로그인 하기 == "); 
		
		System.out.print("▶ 사서아이디: ");
		String libid = sc.nextLine();
		
		System.out.print("▶ 암호: ");
		String pwd = sc.nextLine();
		
		Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); 
		
		if(librarianMapObj != null) { // 파일로 부터 객체정보를 얻어온 경우이라면   
			HashMap<String, Librarian> librarianMap = (HashMap<String, Librarian>) librarianMapObj;	
			
			lib = librarianMap.get(libid);
			
			if(lib != null && lib.getLbid().equals(libid) && lib.getPwd().equals(pwd)) {
				System.out.println(">>> 로그인 성공!!! <<<");
				return lib;
			}
			
			else {
				System.out.println(">>> 로그인 실패!!! <<<");
				return null;	
			}
		}
		
		return lib;
		
	}// end of public Librarian loginLib(Scanner sc)------------------------------------ 
	
	
	
	// ~~~~~~~~~~ 5. 도서정보(ISBN)객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 도서정보등록(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerBookInformation(Scanner sc) {
		
		System.out.println("\n== 도서정보 등록하기 ==");
		
		// 국제표준도서번호(ISBN) 중복검사하기 
		String isbn = "";
		
		do {
			System.out.print("▶ 국제표준도서번호(ISBN): "); // 111-22-33333-44-5 , 111-22-33333-44-6 ,  111-22-33333-44-7
			isbn = sc.nextLine();
						
			if(isbn==null || isbn.trim().isEmpty()) {
				System.out.println("~~~ 국제표준도서번호(ISBN)를 입력하세요!!");
				continue;
			}
			else if(!isUseISBN(isbn)) {
				System.out.println("~~~ "+isbn+" 는 이미 존재하므로 다른 국제표준도서번호(ISBN)를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
		
		System.out.print("▶도서분류카테고리: ");  // Programming , DataBase ,  Programming
		String category = sc.nextLine();
		
		System.out.print("▶도서명: ");         // JAVA기초 , Oracle기초 , Spring 
		String bookname = sc.nextLine();
		
		System.out.print("▶작가명: ");         // 서영학 , 이순신 , 서영학 
		String authorname = sc.nextLine();
		
		System.out.print("▶출판사: ");         // 쌍용출판사 , 강북출판사  ,  쌍용출판사
		String publisher = sc.nextLine();
		
		// 유효성 검사
		int price = 0;
		do {
			System.out.print("▶ 가격 : ");     // 20000 , 25000 , 30000
			String sPrice = sc.nextLine();
			try {
				 price = Integer.parseInt(sPrice);
				 break;
			} catch(NumberFormatException e) {
				System.out.println("~~~~ 오류 : 가격은 숫자로만 입력하세요!!!\n");
			}
		} while (true);
				
		BookInformation bookInformation = new BookInformation(isbn, category, bookname, authorname, publisher, price);
				
		File file = new File(BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 에 해당하는 파일객체 생성하기
		Map<String, BookInformation> bookInformationMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			bookInformationMap = new HashMap<>();
			bookInformationMap.put(isbn, bookInformation);
			n = serial.objectToFileSave(bookInformationMap, BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 bookInformationMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입시
			Object bookInformationMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 저장된 객체를 불러온다.
			bookInformationMap = (HashMap<String, BookInformation>) bookInformationMapObj;
			bookInformationMap.put(isbn, bookInformation);
			n = serial.objectToFileSave(bookInformationMap, BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 bookInformationMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 도서정보등록 성공!! <<<");
		else
			System.out.println(">>> 도서정보등록 실패!! <<<");
		
	}// end of public void registerBookInformation(Scanner sc)-----------------------------------------
	
	
	// == 5.1 국제표준도서번호(ISBN) 중복 검사하기 ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseISBN(String isbn) {
		
		boolean isUse = true; // 최초로 가입시 파일에 저장된 bookInformationMapObj 객체가 없기애 중복된 ISBN 이 없으므로 사용가능하도록 한다.
		
		Object bookInformationMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		
		if(bookInformationMapObj != null) { // 파일에 저장된 bookInformationMapObj 객체가 있다라면 
			HashMap<String, BookInformation> bookInformationMap = (HashMap<String, BookInformation>)bookInformationMapObj;
			BookInformation bookinfo = bookInformationMap.get(isbn);
			if(bookinfo != null) { // ISBN 존재하는 경우이라면 
				isUse = false;
			}
		}
		 
		return isUse;
	}// end of public boolean isUseISBN(String isbn)-----------------------------------
	
	
	
	// ~~~~~~~~~~ 6. 개별도서객체(List)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 6.1 개별도서객체(List) 등록 ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerRentalBook(Scanner sc) {
		
		System.out.println("\n== 개별도서 등록하기 ==");
		
		System.out.print("▶ 국제표준도서번호(ISBN): ");  // 111-22-33333-44-5
		String isbn = sc.nextLine();
		
		Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE);  // BOOKINFOLIST_FILE 파일에 저장된 도서정보객체(Map) 정보를 가져온다.
		Map<String, BookInformation> bookInfoMap = (HashMap<String, BookInformation>) bookInfoMapObj;  
		BookInformation bookInfo = bookInfoMap.get(isbn); // 국제표준도서번호(ISBN)에 해당하는 도서정보 객체를 가져온다.
		
		if(bookInfo == null) {
			System.out.println(">>> 등록된 ISBN 이 아닙니다. 도서등록 실패!! <<<");
			return; 
		}
		
		// 개별도서 아이디 중복 검사하기 
		String bookid = null;
		
		do {
			System.out.print("▶ 도서아이디: ");    
			bookid = sc.nextLine();
			// 111-22-33333-44-5-001 ,  
			// 111-22-33333-44-5-002 , 
			// 111-22-33333-44-5-003 , 
			// 111-22-33333-44-6-001 , 
			// 111-22-33333-44-6-002 , 
			// 111-22-33333-44-7-001
			
			if(bookid==null || bookid.trim().isEmpty()) {
				System.out.println("~~~ 도서아이디를 입력하세요!!");
				continue;
			}
			else if(!isUseBookID(bookid)) {
				System.out.println("~~~ "+bookid+" 는 이미 존재하므로 다른 도서아이디를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
		
		
		RentalBook rentalBook = new RentalBook(isbn, bookid, bookInfo);
					
		File file = new File(RENTALBOOKLIST_FILE);
		List<RentalBook> rentalBookList = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 최초로 도서입력시 
			rentalBookList = new ArrayList<>();
			rentalBookList.add(rentalBook);
			n = serial.objectToFileSave(rentalBookList, RENTALBOOKLIST_FILE); 	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 도서입력시
			Object rentalBookListObj = serial.getObjectFromFile(RENTALBOOKLIST_FILE); 
			rentalBookList = (List<RentalBook>) rentalBookListObj;
			rentalBookList.add(rentalBook);
			n = serial.objectToFileSave(rentalBookList, RENTALBOOKLIST_FILE);
		}
		
		if(n==1)
			System.out.println(">>> 도서등록 성공!! <<<");
		else
			System.out.println(">>> 도서등록 실패!! <<<");
		
	}// end of public void registerRentalBook(Scanner sc)------------------------------------	

	
	
	// ~~~~~~~~~~ 6.2 개별도서 아이디 중복 검사하기  ~~~~~~~~~~ 
	// == 개별도서 아이디 중복 검사하기 ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseBookID(String bookid) {
		
		boolean isUse = true; // 최초로 가입시 파일에 저장된 rentalBookListObj 객체가 없기애 중복된 도서아이디가 없으므로 사용가능하도록 한다.
		
		Object rentalBookListObj = serial.getObjectFromFile(RENTALBOOKLIST_FILE); 
		
		if(rentalBookListObj != null) { // 파일에 저장된 rentalBookListObj 객체가 있다라면 
			List<RentalBook> rentalBookList = (List<RentalBook>)rentalBookListObj;
			for(RentalBook rentalBook : rentalBookList) {
				if(bookid.equals(rentalBook.getBookid())) { // 도서아이디가 존재하는 경우이라면 
					isUse = false;
					break;
				}
			}
		}
		 
		return isUse;
	}// end of public boolean isUseBookID(String bookid)-----------------------------------	
	

	// ~~~~~~~~~~ 10. 도서대여하기  ~~~~~~~~~~ 
	// 대여자는 도서대여를 위해 대여도서를 사서에게 건네주면 
	// 사서는 대여목록에서 대여자의 미반납된 도서중 반납예정일이 지난 미반납된 도서가 있다라면 
	// 도서대여를 불가하다라는 메시지가 출력되도록 한다. 반납예정일은 대여일로 부터 3일뒤가 되도록 한다. 
	@SuppressWarnings("unchecked")
	@Override
	public void rental(Scanner sc) { // 도서대여하기 
		
		System.out.println("\n >>> 도서대여하기 <<<");
		
		String userid = "";  // 도서를 대여해갈 회원ID
		do {
			System.out.print("▶ 회원ID: ");
			userid = sc.nextLine();
			
			if(isUseUserID(userid)) {
				System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~\n");
			}
			else {
				break;
			}
		} while(true);
		
		
		File file = new File(RENTALTASKLIST_FILE); // 대여업무 파일
		List<RentalTask> rentalTaskList = null;
		
		if(!file.exists()) { // 대여업무 파일이 존재하지 않는 경우. 최초로 대여입력시 
			rentalTaskList = new ArrayList<>();
		}
		else { // 대여업무 파일이 존재하는 경우. 두번째 이후로 대여입력시
			Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
			rentalTaskList = (List<RentalTask>) rentalTaskListObj;
			
			// === 도서를 대여해가려는 회원의 미반납된 도서중 반납예정일이 지난 미반납된 도서가 있는지 알아봐서 있다라면 대여를 불가하도록 만든다. ===
			for(RentalTask rt : rentalTaskList) {
				if(rt.getUserid().equals(userid)) {
					try {
						SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd"); 
						String currentDay = sdfmt.format(new Date());
					
						Date currentDate = sdfmt.parse(currentDay);
					//	Date currentDate = sdfmt.parse("2020-08-15");
						Date scheduledReturnDate = sdfmt.parse(rt.getScheduledReturnDay());
						
						long diff = currentDate.getTime() - scheduledReturnDate.getTime();
						
						if(diff > 0) {
							System.out.println("~~~~~ 반납예정일을 넘긴 미반납된 도서가 존재하므로 도서대여가 불가능합니다.!!!");
							return; // 메소드 종료
						}
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
			}// end of for------------------------------------------
			
		}// end of if~else------------------------------------------
		
		// === 개별도서 객체에 대여가능여부를 알아본후 true(비치중)을 false(대여중)로 변경하기 위해 rentalBookList 를 불러옴 시작 === 
		Object rentalBookListObj = serial.getObjectFromFile(RENTALBOOKLIST_FILE); 
		List<RentalBook> rentalBookList = (List<RentalBook>) rentalBookListObj;
		// === 개별도서 객체에 대여가능여부를 알아본후 true(비치중)을 false(대여중)로 변경하기 위해 rentalBookList 를 불러옴 끝 === 
		
		int nTotalCount = 0;
		do {
			System.out.print("▶ 총대여권수: ");
			String sTotalCount = sc.nextLine();
			try {
				nTotalCount = Integer.parseInt(sTotalCount);
				if(nTotalCount < 1) {
					System.out.println("~~~ 총대여권수는 1 이상이어야 합니다. ~~~\n");
					continue;
				}
				else {
					break;
				}
			} catch(NumberFormatException e) {
				System.out.println("~~~ 숫자로만 입력하세요 ~~~\n");
			}
		} while(true);
		
		
		// === 대여해갈 도서ID 입력하기 === 
		for(int i=0; i<nTotalCount; i++) {
			
			String bookid = "";
			do {
				System.out.print("▶ 도서ID: ");
				bookid = sc.nextLine();
				
				if(isUseBookID(bookid)) {
					System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~\n");
				}
				else {
					break;
				}
			} while(true);
		
			// === 대여중(rentalAvailable 값이 false)인 도서ID를 입력하면 대여가 불가하도록 만든다. 시작 ===
			boolean flag = false;
			for(RentalBook rtbook : rentalBookList) {
				if(bookid.equals(rtbook.getBookid()) && !rtbook.isRentalAvailable()) {
					System.out.println("~~~ 현재 대여중인 도서ID 입니다. 새로운 도서ID를 입력하세요!!");
					flag = true;
					break;
				}
			}
			if(flag) {
				i--;
				continue;
			} // === 대여중(rentalAvailable 값이 false)인 도서ID를 입력하면 대여가 불가하도록 만든다. 끝 ===
			
			
			RentalTask rt = new RentalTask();
			rt.setUserid(userid);
			rt.setBookid(bookid);
			rt.setRentalDay();
			rt.setScheduledReturnDay();
			
			rentalTaskList.add(rt);
			
			// === 개별도서 객체에 대여가능여부를 false(대여중)로 변경하기. 시작 === //
			for(int j=0; j<rentalBookList.size(); j++) {
				if( bookid.equals(rentalBookList.get(j).getBookid()) ) {
					rentalBookList.get(j).setRentalAvailable(false);
					break;
				}
			} // === 개별도서 객체에 대여가능여부를 false(대여중)로 변경하기. 끝 === //
			
			
		}// end of for---------------------------------------
		
		int n = serial.objectToFileSave(rentalTaskList, RENTALTASKLIST_FILE); 	
				
		if(n==1) {
			System.out.println(">>> 대여등록 성공!! <<<");
			int m = serial.objectToFileSave(rentalBookList, RENTALBOOKLIST_FILE); 
			if(m==1) {
				System.out.println(">>> 대여도서 비치중에서 대여중으로 변경함 <<<");
			}
		}	
		else
			System.out.println(">>> 대여등록 실패!! <<<");
		
	}// end of public void rental(Scanner sc)---------------------------  
	
	
	
	// ~~~~~~~~~~ 11. 사서는 대여중인 도서에 대해 모든 정보를 조회할 수 있도록 한다. ~~~~~~~~~~
	@SuppressWarnings("unchecked")
	@Override
	public void rentAllView() {  // 사서는 대여중인 도서에 대해 모든 정보를 조회할 수 있도록 한다.
	
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTask> rentalTaskList = (List<RentalTask>) rentalTaskListObj;
		
		Object rentalUserMapObj = serial.getObjectFromFile(RENTALUSERLIST_FILE); 
		Map<String, RentalUser> rentalUserMap = (HashMap<String, RentalUser>) rentalUserMapObj;
		
		Object bookInformationMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		Map<String, BookInformation> bookInformationMap = (HashMap<String, BookInformation>) bookInformationMapObj;
		
		if(rentalTaskList != null) {
			System.out.println("==========================================================================================================================");
			System.out.println("도서ID                     ISBN                도서명        작가명     출판사      회원ID   회원명   연락처                 대여일자                반납예정일 ");
			System.out.println("==========================================================================================================================");
			
			for(RentalTask rt: rentalTaskList) {
				
				RentalUser rtuser = rentalUserMap.get(rt.getUserid()); // 대여해간 회원정보 알아오기 
				
				int index = rt.getBookid().lastIndexOf("-");
				String isbn = rt.getBookid().substring(0, index);
				BookInformation bookinfo = bookInformationMap.get(isbn); // 대여해간 도서정보 알아오기 
				
				rt.setRtuser(rtuser);     // 대여업무 객체에 회원정보 입력하기
				rt.setBookinfo(bookinfo); // 대여업무 객체에 도서정보 입력하기
				
				System.out.println(rt);   // 대여업무 출력
			}// end of for--------------------------------------------------------------
		}
		
		else {
			System.out.println("~~~ 대여정보가 없습니다  ~~~\n");
		}
	}// end of public void rentAllView()-----------------------------------
	
	
	
	// ~~~~~~~~~~ 13. 대여자가 도서를 반납하기 위해 사서에게 도서를 가져다 주면 사서는 도서를 반납처리를 하는데 연체가 있을시 연체 1일당 100원을 연체료로 부과되어 표시되도록 한다. ~~~~~~~~~~ 
	//                반납된 도서는 대여목록에서 제거하고, 개별도서목록에서 비치중으로 변경한다.
	@SuppressWarnings("unchecked")
	@Override
	public void bookReturn(Scanner sc) { // 도서반납하기 
		
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTask> rentalTaskList = (List<RentalTask>) rentalTaskListObj;
		
		Object rentalBookListObj = serial.getObjectFromFile(RENTALBOOKLIST_FILE); 
		List<RentalBook> rentalBookList = (List<RentalBook>) rentalBookListObj;
		
		System.out.println("\n >>> 도서반납하기 <<<");
		
		int nTotalCount = 0;
		do {
			System.out.print("▶ 총반납권수: ");
			String sTotalCount = sc.nextLine();
			try {
				nTotalCount = Integer.parseInt(sTotalCount);
				if(nTotalCount < 1) {
					System.out.println("~~~ 총반납권수는 1 이상이어야 합니다. ~~~\n");
					continue;
				}
				else {
					break;
				}
			} catch(NumberFormatException e) {
				System.out.println("~~~ 숫자로만 입력하세요 ~~~\n");
			}
		} while(true);
		
		
		// === 반납하는 도서ID 입력하기 === 
		int sumArrears = 0; // 연체료 합계 변수 
		for(int i=0; i<nTotalCount; i++) {
			
			String bookid = "";
			do {
				System.out.print("▶ 반납도서ID: ");
				bookid = sc.nextLine();
				
				if(isUseBookID(bookid)) {
					System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~\n");
				}
				else {
					break;
				}
			} while(true);
			
			
			// === 대여목록에서 삭제하기. 시작 === //
			for(int j=0; j<rentalTaskList.size(); j++) {
				if( bookid.equals(rentalTaskList.get(j).getBookid()) ) {
					
					// === 해당도서에 대한 연체료를 보여준다. 시작 === //
					rentalTaskList.get(j).setArrears();
					System.out.println("도서별 연체료: "+rentalTaskList.get(j).sArrears());
					sumArrears += rentalTaskList.get(j).getArrears();
					// === 해당도서에 대한 연체료를 보여준다. 끝 === //
					
					rentalTaskList.remove(j); // 대여목록에서 삭제하기
					break;
				}
			} // === 대여목록에서 삭제하기. 끝 === //
			
			
			// === 개별도서 객체에 대여가능여부를 true(비치중)로 변경하기. 시작 === //
			for(int j=0; j<rentalBookList.size(); j++) {
				if( bookid.equals(rentalBookList.get(j).getBookid()) ) {
					rentalBookList.get(j).setRentalAvailable(true);
					break;
				}
			} // === 개별도서 객체에 대여가능여부를 false(비치중)로 변경하기. 끝 === //
						
		}// end of for---------------------------------------
		
		
		int n = serial.objectToFileSave(rentalTaskList, RENTALTASKLIST_FILE); 	
		
		if(n==1) {
			System.out.println(">>> 도서반납 성공!! <<<");
			int m = serial.objectToFileSave(rentalBookList, RENTALBOOKLIST_FILE); 
			if(m==1) {
				System.out.println(">>> 대여도서 대여중에서 비치중으로 변경함 <<<");
			}
			
			DecimalFormat df = new DecimalFormat("#,###");
			String sSumArrears = (String)df.format(sumArrears);
			System.out.println("▶ 연체료 총계: " + sSumArrears+"원");
		}	
		else
			System.out.println(">>> 도서반납 실패!! <<<");
		
	}// end of public void bookReturn(Scanner sc)------------------------------
	
	
	
	// ~~~~~~~~~~ 2. 일반회원 전용 메뉴를 만든다. ~~~~~~~~~~
	// == 일반회원 전용메뉴 == 
	@Override
	public void rentalUserMenu(Scanner sc) {
		
		RentalUser rtuser = null;
		
		String sMenuno = "";
		
		do {
			String loginName = (rtuser==null)?"":"["+rtuser.getName()+" 로그인중..]";
			
			String sMenu = "\n>>>> 일반회원 전용 Menu "+loginName+" <<<<\n" + 
				      "1.일반회원가입    2.로그인    3.로그아웃   4.도서검색하기    5.나의대여현황보기    6.나가기\n"+ 
	                  "=> 메뉴번호선택 : ";
		
			System.out.print(sMenu);
		
			sMenuno = sc.nextLine(); // 사서전용 Menu 메뉴번호 선택하기
		
			switch (sMenuno) {
				case "1":  // 일반회원가입
					registerRentalUser(sc);
					break;
					
				case "2":  // 로그인
					rtuser = loginRentalUser(sc);
					break;
					
				case "3":  // 로그아웃
					rtuser = null;
					break;
					
				case "4":  // 도서검색하기
					if(rtuser != null) {
						searchBook(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;
					
				case "5":  // 나의대여현황보기
					if(rtuser != null) {
						rentMyView(rtuser.getUserid());
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;	
					
				case "6":  // 나가기
					
					break;		
					
				
				default:
					System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");
					break;
			}	
			
		} while (!"6".equals(sMenuno));	
		
	}// end of public void rentalUserMenu(Scanner sc)----------------------------------------
	

	// ~~~~~~~~~~ 7. 일반회원 객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 7.1 일반회원가입(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerRentalUser(Scanner sc) {
		
		System.out.println("\n== 일반회원 가입하기 ==");
		String userid = "";
		
		// 일반회원 ID 중복검사하기 
		do {
			System.out.print("▶ 회원ID: ");
			userid = sc.nextLine();  // leess , eomjh
			
			if(userid==null || userid.trim().isEmpty()) {
				System.out.println("~~~ 회원아이디를 입력하세요!!");
				continue;
			}
			else if(!isUseUserID(userid)) {
				System.out.println("~~~ "+userid+" 는 이미 존재하므로 다른 회원ID를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
				
		System.out.print("▶ 암호: ");  // 1234 , 1234
		String pwd = sc.nextLine();
		
		System.out.print("▶ 성명: ");  // 이순신, 엄정화
		String name = sc.nextLine();
		
		System.out.print("▶ 연락처: ");  // 010-2345-6789 , 010-9876-5432
		String phone = sc.nextLine();
				
		RentalUser rtuser = new RentalUser(userid, pwd, name, phone);
				
		File file = new File(RENTALUSERLIST_FILE); // RENTALUSERLIST_FILE 에 해당하는 파일객체 생성하기
		Map<String, RentalUser> rentalUserMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			rentalUserMap = new HashMap<>(); 
			rentalUserMap.put(userid, rtuser);
			n = serial.objectToFileSave(rentalUserMap, RENTALUSERLIST_FILE); // RENTALUSERLIST_FILE 파일에 rentalUserMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입시
			Object rentalUserMapObj = serial.getObjectFromFile(RENTALUSERLIST_FILE); // RENTALUSERLIST_FILE 파일에 저장된 객체를 불러온다.
			rentalUserMap = (HashMap<String, RentalUser>) rentalUserMapObj;
			rentalUserMap.put(userid, rtuser);
			n = serial.objectToFileSave(rentalUserMap, RENTALUSERLIST_FILE); // RENTALUSERLIST_FILE 파일에 rentalUserMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 회원등록 성공!! <<<");
		else
			System.out.println(">>> 회원등록 실패!! <<<");
		
	}// end of public void registerRentalUser(Scanner sc)-----------------------------------------

	
	// == 7.2 일반회원 ID 중복검사하기  ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseUserID(String userid) {
		
		boolean isUse = true; // 최초로 가입시 파일에 저장된 rentalUserMapObj 객체가 없기애 중복된 회원아이디가 없으므로 사용가능하도록 한다.
		
		Object rentalUserMapObj = serial.getObjectFromFile(RENTALUSERLIST_FILE); 
		
		if(rentalUserMapObj != null) { // 파일에 저장된 rentalUserMapObj 객체가 있다라면 
			HashMap<String, RentalUser> rentalUserMap = (HashMap<String, RentalUser>)rentalUserMapObj;
			RentalUser rtuser = rentalUserMap.get(userid);
			if(rtuser != null) { // 회원아이디가 존재하는 경우이라면 
				isUse = false;
			}
		}
		
		return isUse;
	}// end of public boolean isUseUserID(String userid)-----------------------------------	
	
		
	// ~~~~~~~~~~ 8. 회원으로 로그인 하기 ~~~~~~~~~~ 
	@SuppressWarnings("unchecked")
	@Override
	public RentalUser loginRentalUser(Scanner sc) { // 일반회원으로 로그인 하기
	
		RentalUser rtuser = null;
		
		System.out.println("\n == 로그인 하기 == "); 
		
		System.out.print("▶ 회원아이디: ");
		String userid = sc.nextLine();
		
		System.out.print("▶ 암호: ");
		String pwd = sc.nextLine();
		
		Object rentalUserMapObj = serial.getObjectFromFile(RENTALUSERLIST_FILE); 
		
		if(rentalUserMapObj != null) { // 파일로 부터 객체정보를 얻어온 경우이라면   
			HashMap<String, RentalUser> rentalUserMap = (HashMap<String, RentalUser>) rentalUserMapObj;	
			
			rtuser = rentalUserMap.get(userid);
			
			if(rtuser != null && rtuser.getUserid().equals(userid) && rtuser.getPwd().equals(pwd)) {
				System.out.println(">>> 로그인 성공!!! <<<");
				return rtuser;
			}
			
			else {
				System.out.println(">>> 로그인 실패!!! <<<");
				return null;	
			}
		}
		
		return rtuser;
		
	}// end of public RentalUser loginRentalUser(Scanner sc)------------------------------------ 	
	
	
	// ~~~~~~~~~~ 9. 도서 검색하기  ~~~~~~~~~~
	// 대여자는 아래에 제시된 정보를 토대로 도서정보(ISBN, 도서아이디, 도서명, 작가명, 출판사, 가격, 대여상태)를 검색할 수 있도록 한다.
	// (1) 도서분류카테고리 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (2) 도서명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (3) 작가명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (4) 출판사명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	
	@SuppressWarnings("unchecked")
	@Override // 도서 검색하기 
	public void searchBook(Scanner sc) { 
		
		System.out.println("\n >>> 도서검색하기 <<<"); 
		
		System.out.println("[주의사항] 검색어를 입력치 않고 엔터를 하면 검색대상에서 제외됩니다.");
		
		System.out.print("▶ 도서분류카테고리(Programming , DataBase 등): ");
		String category = sc.nextLine();
		if(category.trim().isEmpty()) {
			category = "";
		}
		
		System.out.print("▶ 도서명: ");
		String bookname = sc.nextLine();
		if(bookname.trim().isEmpty()) {
			bookname = "";
		}
		
		System.out.print("▶ 작가명: ");
		String authorname = sc.nextLine();
		if(authorname.trim().isEmpty()) {
			authorname = "";
		}
		
		System.out.print("▶ 출판사명: ");
		String publisher = sc.nextLine();
		if(publisher.trim().isEmpty()) {
			publisher = "";
		}
		
		Object rentalBookListObj = serial.getObjectFromFile(RENTALBOOKLIST_FILE); 
		
		if(rentalBookListObj == null) {
			System.out.println("~~~~ 비치된 도서가 없습니다. ~~~~");
		}
		else {
			System.out.println("========================================================================================");
			System.out.println("ISBN                 도서아이디                                도서명           작가명     출판사      가격         대여상태");
			System.out.println("========================================================================================");
			List<RentalBook> rentalBookList = (List<RentalBook>) rentalBookListObj;
			
			boolean flag = false;
			for(RentalBook rtbook : rentalBookList) {
				
				BookInformation bookinfo = rtbook.getBookInfo();
				boolean b_category = bookinfo.getCategory().toLowerCase().startsWith(category.toLowerCase());
				boolean b_bookname = bookinfo.getBookname().toLowerCase().startsWith(bookname.toLowerCase());
				boolean b_authorname = bookinfo.getAuthorname().toLowerCase().startsWith(authorname.toLowerCase());
				boolean b_publisher = bookinfo.getPublisher().toLowerCase().startsWith(publisher.toLowerCase());
				
				if(b_category && b_bookname && b_authorname && b_publisher) {
					flag = true;
					System.out.println(rtbook);
				}
				
			}// end of for---------------------------
			
			if(!flag) {
				System.out.println("~~~~ 검색에 일치하는 도서가 없습니다. ~~~~");
			}
		}
		
	}// end of public void searchBook(Scanner sc)------------------------------------------------
	
	
	// ~~~~~~~~~~ 12. 대여자는 자신이 대여한 도서대여 정보를 조회할 수 있도록 한다. ~~~~~~~~~~
	@SuppressWarnings("unchecked")
	@Override
	public void rentMyView(String userid) {  // 대여자는 자신이 대여한 도서대여 정보를 조회할 수 있도록 한다.
	
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTask> rentalTaskList = (List<RentalTask>) rentalTaskListObj;
		
		Object bookInformationMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		Map<String, BookInformation> bookInformationMap = (HashMap<String, BookInformation>) bookInformationMapObj;
		
		if(rentalTaskList != null) {
			System.out.println("========================================================================================================");
			System.out.println("도서ID                  ISBN                도서명        작가명     출판사      회원ID     대여일자          반납예정일 ");
			System.out.println("========================================================================================================");
			
			boolean flag = false;
			for(RentalTask rt: rentalTaskList) {
				
				if(userid.equals(rt.getUserid())) {
				    flag = true; 
					
					int index = rt.getBookid().lastIndexOf("-");
					String isbn = rt.getBookid().substring(0, index);
					BookInformation bookinfo = bookInformationMap.get(isbn); // 대여해간 도서정보 알아오기 
					
					rt.setBookinfo(bookinfo); // 대여업무 객체에 도서정보 입력하기
					
					System.out.println(rt.getBookid()+ "   " +
					                   rt.getBookinfo().getIsbn()+ "   " +
							           rt.getBookinfo().getBookname()+ "   " +
					                   rt.getBookinfo().getAuthorname()+ "   " + 
					                   rt.getBookinfo().getPublisher()+ "   " + 
					                   userid+ "   " +
					                   rt.getRentalDay()+ "   " +
					                   rt.getScheduledReturnDay() );   // 대여업무 출력
				}
				
			}// end of for--------------------------------------------------------------
			
			if(!flag) {
				System.out.println("~~~ 대여해가신 도서가 없습니다.  ~~~\n");
			}
		}
		
		else {
			System.out.println("~~~ 대여정보가 없습니다  ~~~\n");
		}
	}// end of public void rentMyView()-----------------------------------
	
	
}
