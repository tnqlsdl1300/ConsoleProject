package project.test.serialization;

import java.io.Serializable;

public class Student implements Serializable{

	private static final long serialVersionUID = 439616180046765748L;
	private String name;
	private int age;
	private String address;
	
	public Student(){	}
	
	public Student(String name, int age, String address) {
		super();
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {	// Object에서 상속받은 toString() 메서드 오버라이딩 
		return "학생명: " + name + ", 나이: " + age + ", 주소: " + address;
	}
	
}
