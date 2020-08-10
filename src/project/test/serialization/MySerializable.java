package project.test.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MySerializable {
	
	// 직렬화하는 메서드 생성하기(메모리상에 올라온 객체를 하드디스크 파일에 저장시키기)
	public void objectToFileSave(Object obj, String saveFilename) {	// 객체를 받음
		
		// === 객체 obj를 파일 saveFilename 으로 저장하기 ===
		try {
			
			// 출력노드 스트림(빨대꽂기)
			// 파일이름(saveFilename)을 이용해서 FileOutputStream 객체를 생성한다
			// 생성된 객체는 두번째 파라미터 boolean append 값에 따라서 true 이면 기존 파일에 내용을 덧붙여 추가할 것이고
			// boolean append가 false 이면 기존 내용은 삭제하고 새로운 내용이 기록되도록 하는 것이다
			FileOutputStream fost = new FileOutputStream(saveFilename, false);
			
			
			// 필터스트림(노드 스트림에 속도 향상)
			BufferedOutputStream bufOst = new BufferedOutputStream(fost, 1024);
			
			// 객체 obj를 파일명 saveFilename 에 기록하는(저장하는) 스트림 객체 생성하기
			ObjectOutputStream objOst = new ObjectOutputStream(bufOst);
			
			// 객체 obj를 파일명 saveFilename에 기록하는(저장하는) 것이다
			objOst.writeObject(obj);
			
			
			objOst.close();// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			bufOst.close();// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			fost.close();// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	// 역직렬화하는 메서드 생성하기(하드디스크에 저장된 파일을 읽어다가 객체로 만들어 메모리에 올라가게 하는 것)
	
	public Object getObjectFromFile(String fileName) {
		
		// === 파일명 fileName 을 읽어서 객체 Object로 변환하기
		
		try {
			// 입력노드스트림(빨대꽂기)
			FileInputStream finst= new FileInputStream(fileName);
			
			// 필터스트림(노드 스트림에 속도 향상)
			BufferedInputStream bufInst = new BufferedInputStream(finst, 1024);
			
			// 파일명 fileName 을 읽어서 객체로 만들어주는 스트림 생성해주는 객체	
			ObjectInputStream objInst = new ObjectInputStream(bufInst);
			
			// 파일명 fileName을 읽어서 객체로 만들어주는 것이다
			Object obj = objInst.readObject();
			
			objInst.close();	// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			bufInst.close();	// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			finst.close();		// 사용된 자원반납하기(사용된 객체를 메모리 공간에서 삭제하기)
			
			return obj;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}







































