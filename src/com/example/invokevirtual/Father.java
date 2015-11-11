package com.example.invokevirtual;

public class Father {
	public void fMe(){
        System.out.println("fMe Facther");
        fMe1();//invovespecial调用
        System.out.println(this);
        this.fMe1();//invovespecial调用
    }
     
    private void fMe1(){
        System.out.println("fMe1 Facther");
    }
}
