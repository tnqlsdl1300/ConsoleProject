package test.interfacenouse;

public class BDeveloper {
	
	public void nameRepeat(String name, int count) {
		
		if(name == null || name.trim().isEmpty()) {
			System.out.println("~~~ 성명은 공백은 안됩니다 올바르게 입력하세여!!");
			return;
		}
		
		if(count < 1) {
			System.out.println("~~~ 반복횟수는 1이상 이어야 합니다");
			return;
		}
		
		for(int i = 0; i < count; i++) {
			System.out.println((i+1) + " " + name);
		}
				
	}
	
}
