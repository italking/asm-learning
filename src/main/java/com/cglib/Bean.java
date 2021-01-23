package com.cglib;

import java.beans.PropertyChangeListener;
/**
 * 
 * @author gang
 *
 */
public abstract class Bean implements java.io.Serializable{
   
  String sampleProperty;
  /**
   * 添加监听类，CGLIB 代理 会动态实现此方法  
   * @param listener
   */
  abstract public void addPropertyChangeListener(PropertyChangeListener listener); 
  /**
   * 删除监听类，CGLIB 代理 会动态实现此方法  
   * @param listener
   */
  abstract public void removePropertyChangeListener(PropertyChangeListener listener);
   
   public String getSampleProperty(){
      return sampleProperty;
   }
   
   public void setSampleProperty(String value){
      this.sampleProperty = value;
   }
   
   public String toString(){
     return "sampleProperty is " + sampleProperty;
   }
    
}