package test.interfaceuse;

public class BDeveloper implements InterDeveloper {

	@Override
	public void repeat(String name, int cnt) {
		if(name == null || name.trim().isEmpty()) {
			System.out.println("~~~ 성명은 공백은 안됩니다 올바르게 입력하세여!!");
			return;
		}
		
		if(cnt < 1) {
			System.out.println("~~~ 반복횟수는 1이상 이어야 합니다");
			return;
		}
		
		for(int i = 0; i < cnt; i++) {
			System.out.println((i+1) + " " + name);
		}

	}

}
