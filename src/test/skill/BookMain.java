package test.skill;

public class BookMain {
	
	public static void main(String[] args) {
		
		// Book 이라는 클래스의 객체를 5개를 저장할 수 있는 배열 bookArray를 생성하세요
		
		Book[] bookArray = new Book[5];
		int sumPirce = 0;
		int sumDiscount = 0;
		
		bookArray[0] = new Book("IT", "SQL Plus", 50000, 5);
		bookArray[1] = new Book("IT", "Java 2.0", 40000, 3);
		bookArray[2] = new Book("IT", "JSP Servlet", 60000, 6);
		bookArray[3] = new Book("Noble", "davinchicode", 30000, 10);
		bookArray[4] = new Book("Noble", "cloven hoot", 50000, 15);
		
		System.out.println("==============================================================");
		System.out.println("카테고리\t도서명\t\t정가\t\t할인율\t판매세일가");
		System.out.println("==============================================================");
		for(int i = 0; i < bookArray.length; i++) {
			sumPirce += bookArray[i].getBookPrice();
			sumDiscount += bookArray[i].getDiscount();
			System.out.println(bookArray[i].getCategory() + "\t" + bookArray[i].getBookName() + "\t" + bookArray[i].getBookPrice() + "원\t\t"
					+ bookArray[i].getBookDiscountRate() + "%\t" + bookArray[i].getDiscount() + "원");
		}
		System.out.println("==============================================================");
		System.out.println("판매정가합 : " + sumPirce + "원\t" + "판매세일가합: " + sumDiscount + "원");
		
		
	}
}
