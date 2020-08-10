package test.interfaceuse;

public class ADeveloper implements InterDeveloper {

	@Override
	public void repeat(String name, int cnt) {
		
		for(int i = 0; i < cnt; i++) {
			System.out.println((i+1) + "." + name);
		}

	}

}
