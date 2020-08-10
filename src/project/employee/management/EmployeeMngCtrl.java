package project.employee.management;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class EmployeeMngCtrl implements InterEmployeeMngCtrl {

	private final String DEPTLISTFILENAME = "C:/iotestdata/project/employeemng/deptlist.dat";
	private final String EMPLISTFILENAME = "C:/iotestdata/project/employeemng/employeelist.dat";

	private EmpMngSerializable serial = new EmpMngSerializable();

	// == 부서등록 == //
	@SuppressWarnings("unchecked")
	@Override
	public void registerDept(Scanner sc) {

		System.out.println("\n== 부서등록하기 ==");

		// ---------------------------------------------- //
		/*
		 * int nDtno = 0; do {
		 * 
		 * try { System.out.print("▶부서번호: "); // 10 관리부 서울시 강남구 대치동엔터 //몰라엔터 nDtno =
		 * sc.nextInt(); // // sc.nextInt();는 입력되는 데이터의 종결자는 공백 또는 엔터이다. //
		 * sc.nextInt();는 입력된 값이 공백이 있으면 공백을 무시를 하고서 종결자(공백 또는 엔터) 전 까지의 int타입 데이터(10)만
		 * 읽어들여서 // nDtno 변수에 넣어주고 종결자(공백 또는 엔터)를 포함한 나머지 데이터( 관리부 서울시 강남구 대치동엔터)는 버퍼에서
		 * 삭제되지 않고 그대로 남아있게 된다. // break; } catch(InputMismatchException e) {
		 * System.out.println("~~~~~ 오류 : 부서번호는 숫자로만 입력하세요!!!\n"); sc.nextLine(); } }
		 * while (true);
		 * 
		 * System.out.print("▶부서명: "); String sDname = sc.next(); // // sc.next();는 입력되는
		 * 데이터의 종결자는 공백 또는 엔터이다. // sc.next();는 입력된 값이 공백이 있으면 공백을 무시를 하고서 종결자(공백 또는 엔터)
		 * 전 까지의 String타입 데이터(관리부)만 읽어들여서 // sDname 변수에 넣어주고 종결자(공백 또는 엔터)를 포함한 나머지 데이터(
		 * 서울시 강남구 대치동엔터)는 버퍼에서 삭제되지 않고 그대로 남아있게 된다. //
		 * 
		 * System.out.print("▶부서위치: "); String sLoc = sc.nextLine(); // //
		 * sc.nextLine(); 은 버퍼에서 종결자(엔터)를 포함한 모든 String타입 데이터( 서울시 강남구 대치동엔터)를 읽어들여서 //
		 * 종결자(엔터)전까지의 String타입 데이터( 서울시 강남구 대치동)를 sLoc 변수에 넣어주고 종결자(엔터)는 버퍼에서 삭제되어진다.
		 * //
		 * 
		 * System.out.println("~~~~~ 확인용 nDtno:"+nDtno);
		 * System.out.println("~~~~~ 확인용 sDname:"+sDname);
		 * System.out.println("~~~~~ 확인용 sLoc:"+sLoc);
		 * 
		 * // ---------------------------------------------- //
		 */

		// 유효성 검사
		int nDeptno = 0;
		do {
			System.out.print("▶부서번호: "); // 10 20 30
			String deptno = sc.nextLine(); // "10" "뭐요?"

			try {
				nDeptno = Integer.parseInt(deptno);
				break;
			} catch (NumberFormatException e) {
				System.out.println("~~~~ 오류 : 부서번호는 숫자로만 입력하세요!!!\n");
			}

		} while (true);

		System.out.print("▶부서명: "); // 관 리 부 연구부 생산부
		String dname = sc.nextLine(); // 엔터

		System.out.print("▶부서위치: "); // 서울시 강남구 대치동 인천 수원
		String loc = sc.nextLine();

		DeptDTO deptdto = new DeptDTO(nDeptno, dname, loc);

		File file = new File(DEPTLISTFILENAME);
		// DEPTLISTFILENAME 에 해당하는 파일객체 생성하기

		Map<String, DeptDTO> deptMap = null;
		int n = 0;

		if (!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			deptMap = new HashMap<>();
			deptMap.put(String.valueOf(nDeptno), deptdto);
			n = serial.objectToFileSave(deptMap, DEPTLISTFILENAME); // DEPTLISTFILENAME 파일에 deptdto 객체를 저장시킨다.
		} else { // 파일이 존재하는 경우. 두번째 이후로 부서를 가입하는 경우이다.
			Object deptMapObj = serial.getObjectFromFile(DEPTLISTFILENAME); // DEPTLISTFILENAME 파일에 저장된 객체를 불러온다.
			deptMap = (HashMap<String, DeptDTO>) deptMapObj;
			deptMap.put(String.valueOf(nDeptno), deptdto);

			// --------- 확인용 시작 --------- //
			/*
			 * Set<String> keySet = deptMap.keySet(); // "10" "20" for(String key : keySet)
			 * { System.out.println("~~~ 확인용 key:"+key); // 10 20 DeptDTO dto =
			 * deptMap.get(key); System.out.println("~~~ 확인용 dto:"+dto);
			 * 
			 * // 1.부서번호:10 2.부서명:관리부 3.부서위치:서울 // 1.부서번호:20 2.부서명:연구부 3.부서위치:인천 }
			 */
			// --------- 확인용 끝 --------- //

			n = serial.objectToFileSave(deptMap, DEPTLISTFILENAME); // DEPTLISTFILENAME 파일에 deptdto 객체를 저장시킨다.

		}

		if (n == 1) {
			System.out.println(">>> 부서등록 성공!! <<<");
		} else {
			System.out.println(">>> 부서등록 실패!! <<<");
		}

	}// end of public void registerDept(Scanner sc)--------------------------

	// == 사원등록 == //
	@SuppressWarnings("unchecked")
	@Override
	public void registerEmployee(Scanner sc) {

		// 아이디 중복 검사 및 유효성 검사하기
		String id = null;
		do {
			System.out.print("\n▶아이디: "); // leess emojh hongkd youks
			id = sc.nextLine();

			if (id == null || id.trim().isEmpty()) {
				System.out.println("~~~ 아이디를 입력하세요!!");
				// continue;
			} else if (!isUseID(id)) {
				System.out.println("~~~ " + id + " 는 이미 존재하므로 다른 ID를 입력하세요!!");
				// continue;
			} else {
				break;
			}

		} while (true);

		System.out.print("▶암호: "); // 1234 1234 1234 1234
		String pwd = sc.nextLine();

		System.out.print("▶사원명: "); // 이순신 엄정화 홍길동 유관순
		String ename = sc.nextLine();

		System.out.print("▶생년월일(예 1994.08.04): "); // 1994.08.04 2000.09.10 1988.10.01 1998.07.30
		String birthday = sc.nextLine();

		System.out.print("▶주소: "); // 서울시 마포구 서울시 강남구 서울시 강북구 서울시 영등포구
		String address = sc.nextLine();

		System.out.print("▶직급: "); // 사장 부장 과장 사원
		String jik = sc.nextLine();

		// 유효성 검사
		int salary = 0;
		do {
			System.out.print("▶급여: "); // 7000000 6000000 5000000 4000000
			String sSalary = sc.nextLine();

			try {
				salary = Integer.parseInt(sSalary);
				break;
			} catch (NumberFormatException e) {
				System.out.println("~~~~ 오류 : 급여는 숫자로만 입력하세요!!\n");
			}

		} while (true);

		// 유효성 검사
		int deptno = 0;
		DeptDTO mydeptDto = null;
		do {
			System.out.print("▶부서번호: "); // 10 20 30 10
			String sDeptno = sc.nextLine(); // 90을 입력하면 존재하지 않는 부서번호이다.

			/*
			 * 부서정보가 저장되어진 파일에 가서 모든 부서번호를 읽어온다. 읽어온 부서번호들과 위에서 사용자가 입력한 부서번호가 일치하지 않는 경우라면
			 * 존재하지 않는 부서번호임을 띄워주고 다시 부서번호를 입력받도록 하면 된다.
			 */
			Object deptMapObj = serial.getObjectFromFile(DEPTLISTFILENAME);
			Map<String, DeptDTO> deptMap = (Map<String, DeptDTO>) deptMapObj;
			Set<String> keyset = deptMap.keySet();
			boolean flag = false;
			for (String key : keyset) { // 10 20 30
				if (key.equals(sDeptno)) {
					flag = true;
					break;
				}
			} // end of for----------------------------

			if (!flag) {
				System.out.println("~~~ 입력하신 부서번호 " + sDeptno + "는 우리회사에 존재하지 않는 부서번호 입니다\n");
				continue;
			}

			try {
				deptno = Integer.parseInt(sDeptno);
				mydeptDto = deptMap.get(sDeptno);
				break;
			} catch (NumberFormatException e) {
				System.out.println("~~~~ 오류 : 부서번호는 숫자로만 입력하세요!!\n");
			}

		} while (true);

		EmployeeDTO employee = new EmployeeDTO(id, pwd, ename, birthday, address, jik, salary, deptno, mydeptDto);

		File file = new File(EMPLISTFILENAME);
		List<EmployeeDTO> empList = null;

		if (!file.exists()) { // 파일이 존재하지 않는 경우, 최초로 사원을 등록할 경우
			empList = new ArrayList<>();
			empList.add(employee);
		} else { // 파일이 존재하는 경우, 두번째 이후로 사원을 등록할 경우
			Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
			empList = (List<EmployeeDTO>) empListObj;
			empList.add(employee);
		}

		int n = serial.objectToFileSave(empList, EMPLISTFILENAME);

		if (n == 1)
			System.out.println(">>> 사원등록 성공!! <<<");
		else
			System.out.println(">>> 사원등록 실패!! <<<");

	}// end of public void registerEmployee(Scanner sc)-----------------------------

	// == 중복 아이디 검사하기 == //
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUseID(String id) {

		boolean isUse = true;
		// 최초로 가입시 파일에 저장된 empListObj 객체가 없기때문에 즉,중복된 아이디가 없으므로 사용가능하도록 한다.

		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);

		if (empListObj != null) {
			// 파일에 저장된 empListObj 객체가 있다라면

			// 입력받은 id 가 empListObj 객체속에 존재한다라면 사용불가인 아이디이다.
			List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;

			for (EmployeeDTO emp : empList) {
				if (id.equals(emp.getId())) { // 입력한 id 가 이미 존재하는 경우
					isUse = false;
					break;
				}
			} // end of for--------------------------------

		}

		return isUse;
	}

	// == 로그인 ==
	@SuppressWarnings("unchecked")
	@Override
	public EmployeeDTO login(Scanner sc) {

		System.out.println("\n == 로그인 하기 == ");

		System.out.print("▶ 아이디: ");
		String id = sc.nextLine();

		System.out.print("▶ 암호: ");
		String pwd = sc.nextLine();

		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);

		if (empListObj != null) { // 파일로 부터 객체정보를 얻어온 경우이라면
			List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;

			for (int i = 0; i < empList.size(); i++) {
				EmployeeDTO emp = empList.get(i);

				if (id.equals(emp.getId()) && pwd.equals(emp.getPwd())) {
					System.out.println(">>> 로그인 성공!!! <<<");
					return emp;
				}
			} // end of for------------------

			System.out.println(">>> 로그인 실패!!! <<<");
		}

		else { // 파일로 부터 객체정보를 얻어온 것이 없다라면
			System.out.println(">>> 가입한 회원이 없습니다. <<<");
		}

		return null;
	}// end of public EmployeeDTO login(Scanner
		// sc)---------------------------------------------------

	// == 사원관리 메뉴 ==
	@Override
	public void employeeMenu(Scanner sc, EmployeeDTO loginEmp) {

		String sMenuno = "";
		do {
			String sMenu = "\n>>>> 사원관리 Menu [" + loginEmp.getEname() + " 님 로그인중..]<<<<\n"
					+ "1.내정보 보기   2.내정보 변경하기   3.모든사원정보보기   4.사원검색하기    5.사원사직시키기   6.나가기\n" + "=> 메뉴번호선택 : ";

			System.out.print(sMenu);

			sMenuno = sc.nextLine(); // 사원관리 메뉴번호 선택하기

			switch (sMenuno) {
			case "1": // 내정보 보기
				System.out.println("\n== 내정보 ==\n" + loginEmp);
				break;

			case "2": // 내정보 변경하기
				loginEmp = updateMyInfo(loginEmp, sc);
				break;

			case "3": // 모든사원정보보기
				showAllEmployee();
				break;

			case "4": // 사원검색하기
				searchEmployeeMenu(loginEmp, sc);
				break;
			case "5": // 사원사직시키기
				deleteEmployee(loginEmp, sc);
				break;

			case "6": // 나가기
				break;

			default:
				System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");
				break;
			}

		} while (!"6".equals(sMenuno));

	}// end of public void employeeMenu(Scanner sc, EmployeeDTO
		// loginEmp)------------------------------------

	// == 내정보 변경하기 ==
	@SuppressWarnings("unchecked")
	@Override
	public EmployeeDTO updateMyInfo(EmployeeDTO loginEmp, Scanner sc) {

		/*
		 * loginEmp => 현재(로그인된 상태:홍길동씨) 사용자의 EmployeeDTO(홍길동의 데이터만 있음)가 들어있음 - 구정보 emp
		 * => 새로 데이터가 변경 될 예정인(암호, 주소, 직급, 연봉) EmployeeDTO(홍길동씨)가 들어있음 => setter를 이용해 최신
		 * 값을 넣어줄 예정 - 최신 정보 empList => 현재 파일로 저장되어있는 모든 회원의 정보를 불러들임(EmployeeDTO의 배열이라고
		 * 생각하면 편함 - 모든 회원들의 정보 집합[배열]) 파일을 불러들인 뒤,
		 */

		System.out.println("\n== 내정보 변경하기 ==");

		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);

		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;

		EmployeeDTO emp = null;
		int i = 0;
		for (; i < empList.size(); i++) {
			if (loginEmp.getId().equals(empList.get(i).getId())) {
				emp = empList.get(i);
				System.out.println(emp);
				break;
			}
		}

		if (emp != null) {

			System.out.print("\n▶ 암호변경: ");
			String pwd = sc.nextLine();
			if (pwd.trim().isEmpty()) { // 암호변경에 있어서 엔터나 공백만을 입력할 경우 암호를 변경하지 않고 원래의 암호를 그대로 사용하겠음.
				pwd = emp.getPwd();
			}

			System.out.print("▶ 주소변경: ");
			String address = sc.nextLine();
			if (address.trim().isEmpty()) { // 주소변경에 있어서 엔터나 공백만을 입력할 경우 주소를 변경하지 않고 원래의 주소를 그대로 사용하겠음.
				address = emp.getAddress();
			}

			System.out.print("▶ 직급변경: ");
			String jik = sc.nextLine();
			if (jik.trim().isEmpty()) { // 직급변경에 있어서 엔터나 공백만을 입력할 경우 직급을 변경하지 않고 원래의 직급을 그대로 사용하겠음.
				jik = emp.getJik();
			}

			// 유효성 검사
			int salary = 0;
			do {
				System.out.print("▶ 급여변경: ");
				String sSalary = sc.nextLine();
				if (sSalary.trim().isEmpty()) { // 급여변경에 있어서 엔터나 공백만을 입력할 경우 급여를 변경하지 않고 원래의 급여를 그대로 사용하겠음.
					salary = emp.getSalary();
					break;
				} else {
					try {
						salary = Integer.parseInt(sSalary);
						break;
					} catch (NumberFormatException e) {
						System.out.println("~~~~ 오류 : 급여는 숫자로만 입력하세요!!!\n");
					}
				}
			} while (true);

			boolean isOK = false;
			do {
				System.out.print("▷ 변경하시겠습니까?[Y/N] => ");
				String yn = sc.nextLine();
				if ("Y".equalsIgnoreCase(yn)) {
					isOK = true;
					break;
				} else if ("N".equalsIgnoreCase(yn)) {
					isOK = false;
					break;
				} else {
					System.out.println("~~~ Y 또는 N을 입력하셔야 합니다.");
				}
			} while (true);

			if (isOK) {
				emp.setPwd(pwd);
				emp.setAddress(address);
				emp.setJik(jik);
				emp.setSalary(salary);

				// **** ArrayList 타입인 empList 에 저장되어진 EmployeeDTO 객체 삭제하기 **** //
				empList.remove(i); // empList.remove(삭제할 EmployeeDTO 객체의 인덱스번호);

				empList.add(i, emp); // 삭제되어진 그 인덱스(위치)자리에 새로운 EmployeeDTO 객체를 넣어주기

				int n = serial.objectToFileSave(empList, EMPLISTFILENAME);

				if (n == 1) {
					System.out.println(">>> 내정보 변경 성공!! <<<");
					return emp;
				} else {
					System.out.println(">>> 내정보 변경 실패!! <<<");
				}
			}

			else {
				System.out.println(">>> 내정보 변경을 취소하셨습니다. <<<");
			}

		}

		else {
			System.out.println("~~~~ 내정보 변경작업 실패함!!!");
		}

		return loginEmp;
	}// end of public void EmployeeDTO updateMyInfo(EmployeeDTO loginEmp, Scanner
		// sc)---------------------------

	// 모든 사원정보 출력 메서드
	@Override
	public void showAllEmployee() {

		Object empDtoObj = serial.getObjectFromFile(EMPLISTFILENAME);
		@SuppressWarnings("unchecked")
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empDtoObj;

		System.out.println("\n>>> 모든사원 정보 출력 <<<");
		System.out.println(
				"===================================================================================================================");
		String info = "아이디\t암호\t사원명\t생년월일\t\t나이\t주소\t\t직급\t급여\t\t부서번호\t부서명\t부서위치";
		System.out.println(info);
		System.out.println(
				"===================================================================================================================");
		for (int i = 0; i < empList.size(); i++) {

			System.out.print(empList.get(i).getId() + "\t");
			System.out.print(empList.get(i).getPwd() + "\t");
			System.out.print(empList.get(i).getEname() + "\t");
			System.out.print(empList.get(i).getBirthday() + "\t");
			System.out.print(empList.get(i).showAge() + "\t");
			System.out.print(empList.get(i).getAddress() + "\t");
			System.out.print(empList.get(i).getJik() + "\t");
			System.out.print(empList.get(i).getSalaryComma() + "\t");
			System.out.print(empList.get(i).getDeptno() + "\t");
			System.out.print(empList.get(i).getDeptdto().getDname() + "\t");
			System.out.print(empList.get(i).getDeptdto().getLoc() + "\t");

			System.out.println();

		}

	}

	// 사원검색 메뉴 메서드
	@Override
	public void searchEmployeeMenu(EmployeeDTO loginEmp, Scanner sc) {

		do {

			String sMenu = "\n>>>> 사원검색 Menu [" + loginEmp.getEname() + " 님 로그인 중..] <<<<\n"
					+ "1.사원명 검색   2.연령대 검색   3.직급 검색   4.급여범위 검색    5.부서명 검색   6.나가기\n" + "=> 메뉴번호선택 : ";
			System.out.print(sMenu);

			switch (sc.nextLine()) {
			case "1":
				searchEmployeeByName(sc);	// 1. 사원명 검색
				break;
			case "2":
				searchEmployeeByAge(sc);	// 2. 연령대 검색
				break;
			case "3":
				searchEmployeeByJik(sc);	// 3. 직급 검색
				break;
			case "4":
				searchEmployeeBySalary(sc);	// 4. 급여범위 검색
				break;
			case "5":
				searchEmployeeByDname(sc);	// 5. 부서명 검색
				break;
			case "6":
				// 6. 나가기
				return;
			default:
				System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");

			}

		} while (true);

	}

	// 이름으로 사원 검색 메서드
	@Override
	public void searchEmployeeByName(Scanner sc) {

		// empList - 모든 회원 정보 배열
		// empListOnly - 검색한 사람의 정보 배열

		// 전체 회원정보 불러오기 (empList로 받음)
		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
		@SuppressWarnings("unchecked")
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj; // 모든 회원의 정보 배열

		List<EmployeeDTO> empListOnly = new ArrayList<>(); // 사원검색 출력 메서드로 보내줄 ArrayList 이다.

		do {
			// 사원명을 선언, 스캐너로 입력을 받음
			System.out.print("\n>검색할 사원명: ");
			String name = sc.nextLine(); // 입력받은 이름
			
			if(name.trim().isEmpty() || name == null) {
				System.out.println("~~~~ 검색할 사원명을 입력하세요!!");
				continue;
			}

			// 모든회원 정보에서 이름으로 검색해 해당하는 사람의 dto를 추출해서 emp에 담음
			int i = 0;
			for (; i < empList.size(); i++) {
				if (empList.get(i).getEname().equals(name)) { // 모든회원 정보배열에서 이름으로 검색 -> 해당 번호를 찾음
					empListOnly.add(empList.get(i)); // 이름으로 검색한 인원의 정보를 empListOnly 배열에 담음
				}
			}

			// 이름으로 검색해서 일치하는 정보가 없을시 정보메시지를 출력 후 return으로 메서드 종료(밑의 출력 메서드 호출 실행 x)
			if (empListOnly.isEmpty()) {
				System.out.println(">>> 검색하신 연령대 " + name + " 는(은) 존재하지 않습니다. <<<");
				return;
			}
		} while (empListOnly.isEmpty()); // empListOnly가 null일 시 무한반복

		// 사원검색 프린트 메서드 호출
		printEmployee("사원명", empListOnly);

	}

	public void searchEmployeeByAge(Scanner sc) {

		// empList - 모든 회원들의 정보 배열
		// empListChoice - 선택된 회원들의 정보를 담아줄 정보 배열

		// 모든 회원 정보 불러오기
		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);

		// 검색한 회원들을 저장할 List 선언
		@SuppressWarnings("unchecked")
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;

		// 선택된 회원들의 dto를 받을 리스트 객체 선언(객체 생성을 if문 안에서 할 시 객체가 하나만 들어가는 현상이 생김)
		List<EmployeeDTO> empListChoice = new ArrayList<EmployeeDTO>();

		String searchAge = null;
		@SuppressWarnings("unused")
		int nSearchAge = 0;
		do {
			System.out.print("\n>검색할 연령대(예 20대 검색은 20으로 입력): ");
			searchAge = sc.nextLine();
			
			// 입력받은 값이 숫자인지 확인 - 예외처리
			try {
				nSearchAge = Integer.parseInt(searchAge);
			}catch (NumberFormatException e) {
				System.out.println("~~~ 연령대는 숫자로만 입력하세요!!");
				searchAge = null;	// continue; ?
			}
			
		} while (searchAge == null);
		// 연령대를 사용자에게 입력받음
		

		// if문으로 회원들의 나이를 찾아 List에 넣어주기 (회원들의 나이는 int 형으로 받음 showAge())
		int i = 0;
		for (; i < empList.size(); i++) {

			// int 형태로 나이를 검색, 10으로 나눠서 십의자리를 받음
			if ((empList.get(i).showAge() / 10) == (Integer.parseInt(searchAge) / 10)) { // 나이의 십의자리가 겹칠때
				empListChoice.add(empList.get(i)); // 검색된 값을 empListChoice배열에 대입
			}
		}

		// 만약 검색한 연령대가 존재하지 않을시 실행(if문 아래의 프린트 메서드를 호출하지 않기 위해 내부에서 return으로 메서드 종료)
		if (empListChoice.isEmpty()) {
			System.out.println(">>> 검색하신 연령대 " + searchAge + " 는(은) 존재하지 않습니다. <<<");
			return;
		}

		// 사원검색 프린트 메서드 호출 ()
		printEmployee("연령대", empListChoice);

	}

	// 직급 검색 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void searchEmployeeByJik(Scanner sc) {

		// 모든 회원정보 불러오기(ArrayList 선언, 형변환으로 받은 값 집어넣기)
		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;

		// 검색한 회원들을 저장 할 ArrayList 선언
		List<EmployeeDTO> empListChoice = new ArrayList<EmployeeDTO>();

		do {
			// 직급 입력받을 String 변수 선언, 스캐너로 입력받기
			System.out.print("\n> 검색할 직급명: ");
			String jik = sc.nextLine();

			if (jik.trim().isEmpty() || jik == null) {
				System.out.println("~~~~ 검색할 직급명을 입력하세요!!");
				continue;
			}
			
			// for문, if문과 equals()를 이용해 직급 검색 및 ArrayList에 회원정보 대입(어레이 리스트 -> 어레이 리스트 간의 객체 이동)
			int i = 0;
			for (; i < empList.size(); i++) {

				// 입력받은 직급과 회원 정보 내의 직급을 검사하여 겹칠 시 empListChoice 배열에 객체를 넣어준다
				if (jik.equals(empList.get(i).getJik())) {
					empListChoice.add(empList.get(i));
				}
			}
			
			// empListChoice 배열에 값이 없을 시(검색된 값이 없을 시) 출력 후 return으로 메서드 종료 
			if(empListChoice.isEmpty()) {
				System.out.println(">>> 검색하신 직급 " + jik + " 는(은) 존재하지 않습니다. <<<");
				return;
			}
			
		} while (empListChoice.isEmpty());	// empListChoice의 배열이 비어있으면 while문 무한루프
		

		// 사원검색 프린트 메서드 호출()
		printEmployee("직급", empListChoice);

	}
	
	// 급여범위 검색 메서드
	@SuppressWarnings("unchecked")
	@Override
	public void searchEmployeeBySalary(Scanner sc) {
		
		// 파일에서 오브젝트 받아오고 ArrayList선언해서 형변환해서 대입하기
		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;
		
		// 검색해서 해당하는 회원들을 넣을 ArrayList 선언 및 객체 생성
		List<EmployeeDTO> empListChoice = new ArrayList<EmployeeDTO>();
		
		// 급여 String으로 최소값 / 최대값 선언, 스캐너로 입력받기
		String minMoney = null;
		String maxMoney = null;
		int nMinMoney = 0;
		int nMaxMoney = 0;
		do {
			System.out.print("\n>검색할 급여 최소값: ");
			minMoney = sc.nextLine();
			
			System.out.print(">검색할 급여 최대값: ");
			maxMoney = sc.nextLine();
			
			// 급여 int로 최소값 / 최대값 선언
			// 받은 String 급여 값을 int값으로 형변환해서 String에 넣기
			// number 예외처리 해주기
			try {
				nMinMoney = Integer.parseInt(minMoney);
				nMaxMoney = Integer.parseInt(maxMoney);
			} catch (NumberFormatException e) {
				System.out.println("~~~~ 급여는 숫자로만 입력하세요!!\n");
				continue;
			}
			
			break;
		} while (true);
		
		// if문과 for문을 사용해 int급여와 empList의 급여를 비교
		// 비교해서 값이 같으면 empListChoice에 대입
		for(int i = 0; i < empList.size(); i++) {
			if((empList.get(i).getSalary() >= nMinMoney) && (empList.get(i).getSalary() <= nMaxMoney)) {	// 최대값 최소값 사이에 회원 급여가 있으면
				empListChoice.add(empList.get(i));
			}
		}
		
		// 급여 범위에 해당하는 회원이 없으면 실행
		if(empListChoice.isEmpty()) {
			System.out.println(">>> 검색하신 급여범위에 해당하는 사원은 존재하지 않습니다. <<<");
			return;
		}

		DecimalFormat df = new DecimalFormat("#,###"); 
		
		// 사원검색 프린트 메서드 호출()
		System.out.println("\n>>> 급여범위 검색 <<<[" + (String)df.format(nMinMoney) + "원 ~ " + (String)df.format(nMaxMoney) + "원] <<<");
		printEmployee("급여범위", empListChoice);
	}
	
	// 부서명 검색 메서드
	@Override
	public void searchEmployeeByDname(Scanner sc) {
		
		// 파일에서 모든 회원정보 오브젝트 받아오기, list 선언 및 ArrayList에 받은 오브젝트 값 형변환해서 대입
		Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
		@SuppressWarnings("unchecked")
		List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;
		
		// 검색한 회원들을 넣을 ArrayList 선언
		List<EmployeeDTO> empListChoice = new ArrayList<EmployeeDTO>();
		
		do {
			// 받을 부서명 선언, 부서명 입력받기
			System.out.print("\n>검색할 부서명: ");
			String deptName = sc.nextLine();
			
			if(deptName.trim().isEmpty() || deptName == null) {
				System.out.println("~~~~ 검색할 부서명을 입력하세요!!");
				continue;
			}else {
				// for문과 if문으로  부서명 검색
				for(int i = 0; i < empList.size(); i++) {
					if(deptName.equals(empList.get(i).getDeptdto().getDname())) {
						empListChoice.add(empList.get(i));
					}
				}
			}
			
			// 겹치는 값이 없다면 메세지 출력 후 return으로 메서드 종료
			if(empListChoice.isEmpty()) {
				System.out.println(">>> 검색하신 " + deptName + " 는(은) 존재하지 않습니다. <<<");
				return;
			}
				
		} while (empListChoice.isEmpty());	// empListChoice 객체가 비어있을 시 무한루프
		
		// 사원검색 프린트 메서드 호출()
		printEmployee("부서명", empListChoice);
	}
	
	// 사원사직시키기(사장님이 로그인 되었을때만 가능)
	@Override
	public void deleteEmployee(EmployeeDTO loginEmp, Scanner sc) {
		
		/*
		 * loginEmp - 현재 로그인된 사용자 (사장이여야 함)
		 * 
		 */
		
		// 로그인된 사용자가 사장인지 검사 - true일시 밑으로
		if(loginEmp.getJik().equals("사장")) {
			
			
			// 파일에서 모든 회원정보를 불러옴
			// ArrayList를 선언해서 받은 모든회원정보를 형변환 후 대입
			Object empListObj = serial.getObjectFromFile(EMPLISTFILENAME);
			@SuppressWarnings("unchecked")
			List<EmployeeDTO> empList = (List<EmployeeDTO>) empListObj;
			
			// 사직시킬 회원의 EmployeeDTO를 받아줄 값(나중에 파일에 업데이트 시켜줌)
			//EmployeeDTO emp = null;
			
			// 사직시킬 사용자 변수 선언, 스캐너로 입력받기
			System.out.print("\n사직시킬 사원의 Id를 입력: ");
			String id = sc.nextLine();
			
			// for문과 if문을 이용하여 사원Id로 검색
			// 입력한 id와 겹치는 모든 회원정보 배열에 값을 remove()로 삭제
			boolean flag = false;
			for(int i = 0; i < empList.size(); i++) {
				if(empList.get(i).getId().equals(id)) {	// 입력받은 값과 모든회원 정보 배열의 값이 겹치는 경우
					flag = true;
					empList.remove(i);
				}
			}
			// id와 겹치는 회원정보가 없을 시 오류 출력, return으로 메서드 종료
			if(!flag) {
				System.out.println( id + " 회원의 정보가 없습니다.");
				return;
			}
			
			serial.objectToFileSave(empList, EMPLISTFILENAME);
			System.out.println( id +" 회원의 정보가 삭제 완료되었습니다!");
			
		}else {
			System.out.println("사장만 이용할 수 있는 메뉴입니다.\n");
		}

	}
	
	

	// 사원검색 메서드의 출력메서드(출력 레이아웃)
	@Override
	public void printEmployee(String title, List<EmployeeDTO> emList) {

		// emList - 받은 검색한 이름의 정보

		String info = "아이디\t암호\t사원명\t생년월일\t\t나이\t주소\t\t직급\t급여\t\t부서번호\t부서명\t부서위치";

		if(!title.equals("급여범위")) {
			System.out.println("\n>>> " + title + " 검색 <<<");
		}
		System.out.println(
				"===================================================================================================================");
		System.out.println(info);
		System.out.println(
				"===================================================================================================================");
		for (int i = 0; i < emList.size(); i++) {
			System.out.print(emList.get(i).getId() + "\t");
			System.out.print(emList.get(i).getPwd() + "\t");
			System.out.print(emList.get(i).getEname() + "\t");
			System.out.print(emList.get(i).getBirthday() + "\t");
			System.out.print(emList.get(i).showAge() + "\t");
			System.out.print(emList.get(i).getAddress() + "\t");
			System.out.print(emList.get(i).getJik() + "\t");
			System.out.print(emList.get(i).getSalaryComma() + "\t");
			System.out.print(emList.get(i).getDeptno() + "\t");
			System.out.print(emList.get(i).getDeptdto().getDname() + "\t");
			System.out.print(emList.get(i).getDeptdto().getLoc() + "\t");

			System.out.println();
		}

	}

}
