package project.employee.management;

import java.util.List;
import java.util.Scanner;

public interface InterEmployeeMngCtrl {

	void registerDept(Scanner sc);	// 부서등록
	
	void registerEmployee(Scanner sc); // 사원등록
	
	boolean isUseID(String id);	// 중복 아이디 검사하기
	
	EmployeeDTO login(Scanner sc);// 로그인
	
	void employeeMenu(Scanner sc, EmployeeDTO loginEmp);	// 사원관리 메뉴
	
	EmployeeDTO updateMyInfo(EmployeeDTO loginEmp, Scanner sc); // 내정보 변경하기
	
	void showAllEmployee();	// 모든 사원정보 보여주기
	
	//////////////////////////////////////////////////
	
	void searchEmployeeMenu(EmployeeDTO loginEmp, Scanner sc);	// 사원검색하기 메뉴
	
	void searchEmployeeByName(Scanner sc);	// 사원명 검색
	
	void printEmployee(String title, List<EmployeeDTO> emList);	// 사원명 검색결과 출력하기
	
	void searchEmployeeByAge(Scanner sc);	// 연령대 검색
	
	void searchEmployeeByJik(Scanner sc);	// 직급 검색
	
	void searchEmployeeBySalary(Scanner sc);	// 급여범위 검색
	
	void searchEmployeeByDname(Scanner sc);		// 부서명 검색
	
	void deleteEmployee(EmployeeDTO loginEmp, Scanner sc);	// 사원사직시키기(사장님이 로그인 되었을때만 가능)
	
}
