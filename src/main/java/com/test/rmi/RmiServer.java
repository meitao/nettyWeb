package com.test.rmi;

import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;

public class RmiServer {
	public static void main(String args[]){
		try{
		    LocateRegistry.createRegistry(8888);   
			HelloRmiService s1 = new HelloServiceImpl("s1");
			HelloRmiService s2 = new HelloServiceImpl("s2");
			Context contex = new InitialContext();
			contex.rebind("rmi://localhost:8888/s1", s1);
//			contex.rebind("rmi:s2", s2);
//			 Naming.bind("rmi://localhost:8888/s1",s1); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
