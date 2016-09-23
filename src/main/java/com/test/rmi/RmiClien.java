package com.test.rmi;

import java.rmi.Naming;

import javax.naming.Context;
import javax.naming.InitialContext;

public class RmiClien {

	public static void main(String[] args) {
		String url="rmi://localhost:8888/";
		try{
			Context context = new InitialContext();
			
 			HelloRmiService s1 = (HelloRmiService) context.lookup(url+"s1");
//			HelloRmiService s2 = (HelloRmiService) context.lookup(url+"s2");
//			HelloRmiService hello = (HelloRmiService) Naming.lookup(url+"s1"); 
//			System.out.println(hello.echo("hello"));
			System.out.println(s1.echo("hello"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
