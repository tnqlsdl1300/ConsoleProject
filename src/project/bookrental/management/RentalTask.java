package project.bookrental.management;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class RentalTask implements Serializable {

	private static final long serialVersionUID = -5697690995950171124L;
	private String userId; // 유저아이디
	private String bookId; // 책고유 아이디
	private String rentalDay; // 책 대여 날짜
	private String returnDay;// 책 반납 날짜
	private int arrears; // 연체료

	private UserDTO userDto; // 회원객체
	private ISBNDTO isbnDto; // 공통책 정보 객체

	// 대여 dto에 관한 ArrayList를 만들어서 다른dto와 같이 리스트에 넣어주고 파일로 저장시켜준다

	// 도서를 빌릴때 rentalTask 객체에 올바른 회원객체와 도서정보 객체를 넣어야 한다(회원 아이디, 도서아이디로 판별)

	// 대여 날짜 연체를 결정하는 것은 여기서 따로 메서드를 만듬
	// 대여의 경우 년월일 만 계산하고 그 뒤에 시분초는 무시해서 계산한다 - 일주일
	// cal.add(Calendar.DAY_OF_YEAR, 7) => 7일후 -> 날짜를 조정하는법

	public RentalTask() {
	}

	public RentalTask(String userId, String bookId, String rentalDay, String returnDay, int arrears, UserDTO userDto,
			ISBNDTO isbnDto) {
		super();
		this.userId = userId;
		this.bookId = bookId;
		this.rentalDay = rentalDay;
		this.returnDay = returnDay;
		this.arrears = arrears;
		this.userDto = userDto;
		this.isbnDto = isbnDto;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getRentalDay() {
		return rentalDay;
	}

	public void setRentalDay(String rentalDay) {
		this.rentalDay = rentalDay;
	}

	public String getReturnDay() {
		return returnDay;
	}

	public void setReturnDay(String returnDay) {
		this.returnDay = returnDay;
	}

	public int getArrears() {
		return arrears;
	}

	public void setArrears(int arrears) {
		this.arrears = arrears;
	}

	public UserDTO getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDTO userDto) {
		this.userDto = userDto;
	}

	public ISBNDTO getIsbnDto() {
		return isbnDto;
	}

	public void setIsbnDto(ISBNDTO isbnDto) {
		this.isbnDto = isbnDto;
	}


	public void setRentalDay() { // 도서 대여일 설정 메서드

		LocalDateTime now = LocalDateTime.now(); // now 변수에 현재 년월일을 받아옴
		LocalDateTime ldt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);	// 년월일만 받고 그 뒤는 000으로 처리해줌
		rentalDay = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));	// LocalDateTime의 값을 String 타입으로 변환시킨 뒤 필드에 저장

		//System.out.println("대여일: " + rentalDay);
	}

	public void setReturnDay() { // 도서 반납일 설정 메서드
		
		LocalDateTime now = LocalDateTime.now(); // now 변수에 현재 년월일을 받아옴
		LocalDateTime ldt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);	// 년월일만 받고 그 뒤는 000으로 처리해줌
		LocalDateTime fixLdt = ldt.plusDays(7);	// 위 날짜의 일주일 뒤를 저장(반납일)
		returnDay = fixLdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));	// LocalDateTime의 값을 String 타입으로 변환시킨 뒤 필드에 저장

		//System.out.println("반납일: " + returnDay);

	}

	// 연체료를 내주는 메서드
	public int payArrears() {
		
		// LocalDate sRentalDay = LocalDate.parse(rentalDay);	// 대여일
		LocalDate sReturnDay = LocalDate.parse(returnDay);	// 반납일
		//LocalDate now = LocalDate.now();	// 현재날짜
		
		LocalDate now = LocalDate.of(2020,8,20);	// 지정날짜 (현재를 20일이라 가정)

		int n = (int) ChronoUnit.DAYS.between(sReturnDay, now);	// 두 매개변수의 날짜차이만큼을 int형으로 받음
		arrears += n*200;
		
		return n*200;	// 연체료는 날짜수*200??
	}

	// BookIdDto의 대여상태를 바꿔주는 메서드
	@SuppressWarnings("unchecked")
	public void setRentalAvailable(String bookId, boolean rent) {
		
		// 고유 도서 ID DTO를 파일에서 받아옴
		final String BOOKIDLISTFILENAME = "C:/iotestdata/project/bookmng/bookIdlist.dat";
		IsbnMngSerializable serial = new IsbnMngSerializable();
		
		Object obj = serial.getObjectFromFile(BOOKIDLISTFILENAME);
		List<BookIdDTO> bookIdList = (ArrayList<BookIdDTO>) obj;
		
		// 도서ID로 검색해서 겹치는 값이 있을 시 매개변수의 값(true-대여중, false-비치중) 대입
		for(BookIdDTO list : bookIdList) {
			if(bookId.equals(list.getBookId())) {
				list.setRent(rent);
			}
		}
		
		// 파일로 정보 업데이트
		serial.objectToFileSave(bookIdList, BOOKIDLISTFILENAME);
		
	}


}
