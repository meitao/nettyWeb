package com.test.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloRmiService {

	private String name ;
	
	public HelloServiceImpl(String name) throws RemoteException{
		super();
		this.name = name ;
	}
	
	@Override
	public String echo(String msg) throws RemoteException {
		System.out.println(name+">>>>  echo" );
		return "hello echo !";
	}

	@Override
	public Date getTime() throws RemoteException {
		System.out.println(name+">>>>  getTime" );
		return new Date();
	}

}
