package test.skill;

public class Book {
	private String category;		// 도서 카테고리
	private String bookName;		// 도서명
	private int bookPrice;			// 정가
	private int bookDiscountRate;	// 할인가
	
	public Book() {	}
	
	public Book(String category, String bookName, int bookPrice, int bookDiscountRate) {
		this.category = category;
		this.bookName = bookName;
		this.bookPrice = bookPrice;
		this.bookDiscountRate = bookDiscountRate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(int bookPrice) {
		this.bookPrice = bookPrice;
	}

	public int getBookDiscountRate() {
		return bookDiscountRate;
	}

	public void setBookDiscountRate(int bookDiscountRate) {
		this.bookDiscountRate = bookDiscountRate;
	}
	
	
	int getDiscount() {		// 메서드, operation(기능)
		
		double result = bookPrice * ((100 - bookDiscountRate) * 0.01);
		
		return (int)result;
	}
	
	
}
