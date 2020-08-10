package project.employee.management;

import java.util.Scanner;

public class EmployeeMngMainExecution {

	public static void main(String[] args) {
		
		InterEmployeeMngCtrl ctrl = new EmployeeMngCtrl();
		
		Scanner sc = new Scanner(System.in);
		
		String sMenu = "1.부서등록    2.사원등록    3.로그인    4.로그아웃    5.프로그램 종료\n" +
                       "=> 메뉴번호선택 : ";
		
		String sMenuno = "";
		EmployeeDTO loginEmp = null;
		
		do {
			String sMenuFirst = (loginEmp == null)?"\n>>>> Main Menu <<<<":"\n>>>> Main Menu ["+loginEmp.getEname()+" 님 로그인중..]<<<<";  
			System.out.println(sMenuFirst); // 메인 메뉴 상단에 로그인 되어진 사원명 보여주기
			
			System.out.print(sMenu); // 메인 메뉴 보여주기
			sMenuno = sc.nextLine(); // 메인 메뉴번호 선택하기
			
			switch (sMenuno) {
				case "1":  // 부서등록
					ctrl.registerDept(sc);
					break;
			
				case "2":  // 사원등록
					ctrl.registerEmployee(sc);
					break;
					
				case "3":  // 로그인
					if(loginEmp != null) {	// 로그인이 이미 되어있는 상태
						System.out.println("~~~~ 이미 로그인되어진 상태 입니다.!!!");
					}
					
					else {
						loginEmp = ctrl.login(sc);
						if(loginEmp != null) {
							ctrl.employeeMenu(sc, loginEmp);
						}
					}
					break;
					
				case "4":  // 로그아웃
					loginEmp = null;
					System.out.println("== 로그아웃 되었습니다 ==");
					break;
									
				case "5":  // 프로그램종료
					break;
	
				default:
					System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요!!");
					break;
			}// end of switch------------------
			
		} while (!"5".equals(sMenuno)); // end of while-----------------

		System.out.println(">>> 프로그램을 종료합니다. <<<");
		sc.close();		

	}// end of main()-----------------------------

}
