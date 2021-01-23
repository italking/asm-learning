package com.example.annotation;

import java.io.Serializable;

@MyAnnotation(id = 1 , message="message" , names = {"A" , "B" ,"C"} , color = Color.BLUE)
public  class MyClass implements @MyTypeAnnotation Serializable  {

}
