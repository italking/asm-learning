package com.example.trycatch;
/**
 * 测试Try catch的嵌套效果
 * @author gang
 *
 */
public class TryNested {
	
  public  void  test(){
	  
        try {
        	
			try {
				int a = 1/0;
			} catch (RuntimeException e) {
				System.out.println("Second");
			} finally{
				System.out.println("finally");
			}
			
			
		} catch (Exception e) {
			System.out.println("First");
		}
          
  }
}
