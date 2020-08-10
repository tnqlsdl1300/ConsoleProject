package project.employee.management;

import java.io.Serializable;

public class DeptDTO implements Serializable {
	
	// DTO(Data Transfer Object) ==> 데이터전송객체 
	
	private static final long serialVersionUID = -4243660468555022538L;
	private int deptno;    // 부서번호
	private String dname;  // 부서명 
	private String loc;    // 부서위치 
	
	public DeptDTO() {}
	
	public DeptDTO(int deptno, String dname, String loc) {
		this.deptno = deptno;
		this.dname = dname;
		this.loc = loc;
	}

	public int getDeptno() {
		return deptno;
	}

	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "1.부서번호:"+deptno+"  "+
			   "2.부서명:"+dname+"  "+
			   "3.부서위치:"+loc+"\n";
	}

}






