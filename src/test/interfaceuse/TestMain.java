package test.interfaceuse;

public class TestMain {
	public static void main(String[] args) {
		
		InterDeveloper ad = new BDeveloper();	// 객체만 BDeveloper로 바꿔주면 됨. 다형성  up
		ad.repeat("이순신", -3);

		
	}
}
