package project.bookrental.management;

import java.util.Scanner;

public class BookMainExecution {
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		BookRentalMngCtrl ctrl = new BookRentalMngCtrl();
		Scanner sc = new Scanner(System.in);
		String menu = null;
		int nMenu = 0;
		
		System.out.println("===> 도서대여 프로그램 <===");
		
		do {
			System.out.print("1.사서 전용메뉴   2.일반회원 전용메뉴   3.프로그램 종료\n=> 메뉴번호선택 : ");
			menu = sc.nextLine();
			
			try {
				nMenu = Integer.parseInt(menu);
			}catch (Exception e) {
				System.out.println("메뉴선택은 숫자로 입력해 주세요!!!");
			}
			
			switch (menu) {
			case "1":
				ctrl.libMenu(sc);
				break;
			case "2":
				ctrl.UserMenu(sc);
				break;
			case "3":
				System.exit(0);
			default:
				System.out.println("메뉴에 없는 번호입니다.\n다시선택해 주세요!!!");
				break;
			}
			
		} while (true);
		
	}

}
