package project.employee.management;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;

public class EmployeeDTO implements Serializable {

	private static final long serialVersionUID = 2534699851589679551L;
	private String id;
	private String pwd;
	private String ename;
	private String birthday;  // 1996.08.04
	private String address;
	private String jik;
	private int salary;
	private int deptno;
	private DeptDTO deptdto;
	
	
	// 생성자 오버로딩
	public EmployeeDTO() { }
	
	public EmployeeDTO(String id, String pwd, String ename, String birthday, String address, String jik, int salary, int deptno
			          ,DeptDTO deptdto) { 
		this.id = id;
		this.pwd = pwd;
		this.ename = ename;
		this.birthday = birthday;
		this.address = address;
		this.jik = jik;
		this.salary = salary;
		this.deptno = deptno;
		this.deptdto = deptdto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getJik() {
		return jik;
	}

	public void setJik(String jik) {
		this.jik = jik;
	}

	public int getSalary() {
		return salary;
	}
	
	public String getSalaryComma() { 
        // 숫자 세자리 수 마다 콤마찍기
		DecimalFormat df = new DecimalFormat("#,###"); 
		String sSalary = (String)df.format(salary);

		return sSalary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public int getDeptno() {
		return deptno;
	}

	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	
	public DeptDTO getDeptdto() {
		return deptdto;
	}

	public void setDeptdto(DeptDTO deptdto) {
		this.deptdto = deptdto;
	}

	public int showAge() { // 현재나이를 알려준다.
		Calendar currentdate = Calendar.getInstance(); // 현재날짜와 시간을 얻어온다.
		int currentYear = currentdate.get(Calendar.YEAR); // 현재년도를 얻어온다.
		
		return currentYear - Integer.parseInt(birthday.substring(0, birthday.indexOf("."))) + 1;
	}
	
	@Override
	public String toString() {
		String emp = "▷ 아이디: " + id + "\n" +
					 "▷ 암호: " + pwd + "\n" +
					 "▷ 사원명: " + ename + "\n" +
					 "▷ 생년월일: " + birthday + "\n" +
					 "▷ 나이: " + showAge() +  "세\n" +
					 "▷ 주소: " + address + "\n" +
					 "▷ 직급: " + jik + "\n" +
					 "▷급여: " + getSalaryComma() + "\n" +
					 "▷ 부서번호: " + deptno + "\n" +
					 "▷ 부서명: " + deptdto.getDname() + "\n" + 
		             "▷ 부서위치: " + deptdto.getLoc() + "\n";
				
		return emp;        
	}
	
}
