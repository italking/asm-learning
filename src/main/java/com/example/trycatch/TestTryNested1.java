package com.example.trycatch;
/**
 * 测试Try catch的嵌套效果
 * @author gang
 *
 */
public class TestTryNested1 {

	
 public static class ExceptionFirstLevel extends RuntimeException {
	 
 }
 
 public static class ExceptionSecondLevel extends RuntimeException {
	 
 }
 
 public static class ExceptionThreeLevel extends RuntimeException {
	 
 }
 
  public static void main(String[] args) {
	  sayHello();
 }
 
  public static  void  sayHello(){
	  
        try {
        	
			try {
				int a = 1/0;
			} catch (ExceptionSecondLevel e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (ExceptionFirstLevel e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			int a = 1/0;
		} catch (ExceptionThreeLevel e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
  
  } 
 
 
 
}
