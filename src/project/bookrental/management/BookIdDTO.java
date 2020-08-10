package project.bookrental.management;

import java.io.Serializable;

// 도서 ID DTO
public class BookIdDTO implements Serializable {
	
	private static final long serialVersionUID = 9189513404826468175L;
	private String bookId;// 도서 ID
	private String bookISBN;
	private boolean isRent = false; // 대여상태 (대여중 => true, 비치중 => false)
	
	private ISBNDTO isbndto;
	
	
	public BookIdDTO() {	}


	public BookIdDTO(String bookId, String bookISBN, boolean isRent, ISBNDTO isbndto) {
		super();
		this.bookId = bookId;
		this.bookISBN = bookISBN;
		this.isRent = isRent;
		this.isbndto = isbndto;
	}


	public String getBookId() {
		return bookId;
	}


	public void setBookId(String bookId) {
		this.bookId = bookId;
	}


	public String getBookISBN() {
		return bookISBN;
	}


	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}


	public boolean isRent() {
		return isRent;
	}


	public void setRent(boolean isRent) {
		this.isRent = isRent;
	}


	public ISBNDTO getIsbndto() {
		return isbndto;
	}


	public void setIsbndto(ISBNDTO isbndto) {
		this.isbndto = isbndto;
	}
	
	
	// 검색한 도서정보를 출력해줄 메서드
	@Override
	public String toString() {
		
		String rent = null;
		
		if (isRent()) {
			rent = "대여중";
		} else {
			rent = "비치중";
		}
		
		
		return getBookISBN() + 
				"\t\t\t" + getBookId() + 
				"\t\t\t" + getIsbndto().getBookName() +
				"\t\t\t" + getIsbndto().getBookAuthorName() + 
				"\t" + getIsbndto().getBookPublisher() +
				"\t" + getIsbndto().getBookPriceComma() + "원" + 
				"\t" + rent;

	}
	
	
	

}
