package project.bookrental.management;

import java.io.Serializable;
import java.text.DecimalFormat;

// 도서 DTO

public class ISBNDTO implements Serializable {

	private static final long serialVersionUID = -2795128761201979818L;

	private String bookISBN; // 도서 ISBN

	private String bookCatagory; // 도서 카테고리

	private String bookName; // 도서 이름

	private String bookAuthorName; // 저자명

	private String bookPublisher; // 도서 출판사

	private String bookPrice; // 도서 가격

	public ISBNDTO() {
	}

	public ISBNDTO(String bookISBN, String bookCatagory, String bookName, String bookAuthorName, String bookPublisher,
			String bookPrice) {
		super();
		this.bookISBN = bookISBN;
		this.bookCatagory = bookCatagory;
		this.bookName = bookName;
		this.bookAuthorName = bookAuthorName;
		this.bookPublisher = bookPublisher;
		this.bookPrice = bookPrice;
	}

	public String getBookISBN() {
		return bookISBN;
	}

	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}

	public String getBookCatagory() {
		return bookCatagory;
	}

	public void setBookCatagory(String bookCatagory) {
		this.bookCatagory = bookCatagory;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthorName() {
		return bookAuthorName;
	}

	public void setBookAuthorName(String bookAuthorName) {
		this.bookAuthorName = bookAuthorName;
	}

	public String getBookPublisher() {
		return bookPublisher;
	}

	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}

	public String getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(String bookPrice) {
		this.bookPrice = bookPrice;
	}
	
	// 숫자 값에 ,를 찍어주기 위한 메서드
	public String getBookPriceComma() {
		
		DecimalFormat df = new DecimalFormat("#,###");
		
		int nPrice = Integer.parseInt(bookPrice);
		String sPrice = df.format(nPrice);
		
		return sPrice;
	}

}